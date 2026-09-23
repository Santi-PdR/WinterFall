"""Fail fast when a WinterFall visual resource cannot load in Minecraft.

Validates PNG encoding, model/blockstate JSON, local WinterFall texture/model
references and the vanilla model UV range. It uses only the Python standard
library so it is safe to run on a clean Fedora/Forge development checkout.
"""
from __future__ import annotations

import json
import struct
import sys
import zlib
from pathlib import Path

ASSETS = Path(__file__).parents[1] / "src/main/resources/assets/winterfall"
ERRORS: list[str] = []


def fail(message: str) -> None:
    ERRORS.append(message)


def verify_png(path: Path) -> None:
    try:
        data = path.read_bytes()
        if data[:8] != b"\x89PNG\r\n\x1a\n":
            raise ValueError("bad PNG signature")
        position, idat, ihdr = 8, bytearray(), None
        while position < len(data):
            length = struct.unpack(">I", data[position:position + 4])[0]
            chunk_type = data[position + 4:position + 8]
            chunk = data[position + 8:position + 8 + length]
            stored_crc = struct.unpack(">I", data[position + 8 + length:position + 12 + length])[0]
            if zlib.crc32(chunk_type + chunk) & 0xFFFFFFFF != stored_crc:
                raise ValueError(f"CRC mismatch in {chunk_type.decode('ascii')}")
            if chunk_type == b"IHDR":
                ihdr = struct.unpack(">IIBBBBB", chunk)
            elif chunk_type == b"IDAT":
                idat.extend(chunk)
            position += 12 + length
        if position != len(data) or ihdr is None:
            raise ValueError("incomplete PNG chunk stream")
        width, height, bit_depth, colour_type, compression, filtering, interlace = ihdr
        channels = {0: 1, 2: 3, 3: 1, 4: 2, 6: 4}.get(colour_type)
        if not channels or bit_depth != 8 or compression or filtering or interlace:
            raise ValueError(f"unsupported IHDR {ihdr}")
        raw = zlib.decompress(idat)
        stride = width * channels
        expected = height * (stride + 1)
        if len(raw) != expected:
            raise ValueError(f"decoded {len(raw)} bytes; expected {expected}")
        if any(raw[row * (stride + 1)] > 4 for row in range(height)):
            raise ValueError("invalid PNG scanline filter")
    except Exception as error:
        fail(f"PNG {path.relative_to(ASSETS)}: {error}")


def texture_path(texture: str) -> Path | None:
    if not texture.startswith("winterfall:"):
        return None
    namespace_path = texture.split(":", 1)[1]
    return ASSETS / "textures" / f"{namespace_path}.png"


def verify_model(path: Path) -> None:
    try:
        model = json.loads(path.read_text())
    except Exception as error:
        fail(f"JSON {path.relative_to(ASSETS)}: {error}")
        return
    parent = model.get("parent", "")
    if parent.startswith("winterfall:"):
        parent_path = ASSETS / "models" / f"{parent.split(':', 1)[1]}.json"
        if not parent_path.is_file():
            fail(f"model parent missing: {path.relative_to(ASSETS)} -> {parent}")
    textures = model.get("textures", {})
    for key, texture in textures.items():
        target = texture_path(texture)
        if target and not target.is_file():
            fail(f"texture missing: {path.relative_to(ASSETS)} #{key} -> {texture}")
    for element in model.get("elements", []):
        for face_name, face in element.get("faces", {}).items():
            texture = face.get("texture", "")
            if texture.startswith("#") and texture[1:] not in textures:
                fail(f"unbound texture: {path.relative_to(ASSETS)} {face_name} -> {texture}")
            uv = face.get("uv")
            if uv and (len(uv) != 4 or any(not isinstance(value, (int, float)) or value < 0 or value > 16 for value in uv)):
                fail(f"UV outside 0..16: {path.relative_to(ASSETS)} {face_name} -> {uv}")


def verify_blockstate(path: Path) -> None:
    try:
        state = json.loads(path.read_text())
    except Exception as error:
        fail(f"JSON {path.relative_to(ASSETS)}: {error}")
        return
    for variant in state.get("variants", {}).values():
        entries = variant if isinstance(variant, list) else [variant]
        for entry in entries:
            model = entry.get("model", "")
            if model.startswith("winterfall:block/"):
                candidate = ASSETS / "models/block" / f"{model.rsplit('/', 1)[1]}.json"
                if not candidate.is_file():
                    fail(f"blockstate model missing: {path.relative_to(ASSETS)} -> {model}")


for png in sorted((ASSETS / "textures").rglob("*.png")):
    verify_png(png)
for model in sorted((ASSETS / "models").rglob("*.json")):
    verify_model(model)
for blockstate in sorted((ASSETS / "blockstates").glob("*.json")):
    verify_blockstate(blockstate)

if ERRORS:
    print("\n".join(f"ERROR: {message}" for message in ERRORS))
    raise SystemExit(1)
print("WinterFall resource validation passed.")
