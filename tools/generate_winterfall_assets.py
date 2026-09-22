"""Creates original deterministic 32px WinterFall pixel-art textures and item models."""
from pathlib import Path
import json, struct, zlib

ROOT = Path(__file__).parents[1] / "src/main/resources/assets/winterfall"
ITEMS = ["short_ammo","long_ammo","shotgun_shell","billhook_blueprint","field_ration","canteen",
         "combat_knife","billhook","pipe_wrench","cleaver","machete","hatchet","sledgehammer",
         "crowbar","scrap_spear","riot_baton","trench_shovel","broomhandle_22","mark_vii",
         "service_rifle","pump_shotgun","improvised_smg","hunting_rifle","flare_gun",
         "combat_knife_scavenger_spawn_egg"]

def png(path, pixels, w=32, h=32):
    raw = b"".join(b"\0" + b"".join(bytes(px) for px in row) for row in pixels)
    chunk = lambda tag, data: struct.pack(">I",len(data))+tag+data+struct.pack(">I",zlib.crc32(tag+data)&0xffffffff)
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(b"\x89PNG\r\n\x1a\n"+chunk(b"IHDR",struct.pack(">IIBBBBB",w,h,8,6,0,0,0))+chunk(b"IDAT",zlib.compress(raw,9))+chunk(b"IEND",b""))

def texture(idx, block=False):
    p=[[(0,0,0,0) for _ in range(32)] for _ in range(32)]
    steel,light,dark,rust,ember,cloth=(132,151,158,255),(205,224,224,255),(40,50,56,255),(145,75,43,255),(218,159,75,255),(71,93,104,255)
    if block:
        for y in range(32):
            for x in range(32):
                n=(x*13+y*7+idx*17)%23; p[y][x]=(64+n,70+n//2,69+n//3,255)
        for x in range(2,32,8):
            for y in range(32): p[y][x]=dark
        for y in range(4,32,11):
            for x in range(32): p[y][x]=dark
        return p
    for y in range(5,28):
        c=16+(idx%5-2)*(y-16)//14; half=max(2,5-abs(y-16)//6)
        for x in range(c-half,c+half+1):
            if 0<=x<32: p[y][x]=steel if (x+y+idx)%4 else light
    for k in range(5):
        x=(idx*7+k*5)%25+3; y=(idx*11+k*7)%24+4; p[y][x]=rust; p[min(31,y+1)][x]=dark
    if idx%3==0:
        for x in range(7,25): p[14][x]=steel
    elif idx%3==1:
        for y in range(8,24): p[y][19]=cloth
    else:
        for x in range(10,22): p[23][x]=ember if x%2 else rust
    for y in range(32):
        for x in range(32):
            if p[y][x][3] and (x*3+y*5+idx)%11==0: p[y][x]=dark
    return p

for i,name in enumerate(ITEMS):
    png(ROOT/"textures/item"/f"{name}.png", texture(i))
    (ROOT/"models/item"/f"{name}.json").write_text(json.dumps({"parent":"minecraft:item/generated","textures":{"layer0":f"winterfall:item/{name}"}},indent=2)+"\n")
for i,name in enumerate(["scrapper","workbench","workbench_top"]):
    png(ROOT/"textures/block"/f"{name}.png", texture(i,True))
