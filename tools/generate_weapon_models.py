"""Build distinct low-poly Minecraft JSON models for WinterFall's important held weapons.

The output is intentionally authored per weapon family rather than recoloring one parent model.
All geometry is vanilla item-model JSON and uses the original 64px WinterFall weapon atlas.
"""
from pathlib import Path
import json

ROOT = Path(__file__).parents[1] / "src/main/resources/assets/winterfall/models/item"
ATLAS = "winterfall:item/weapon_atlas"
UV = {
    "metal": [0, 0, 16, 16], "dark": [16, 0, 32, 16], "grip": [32, 0, 48, 16],
    "wood": [48, 0, 64, 16], "rust": [0, 16, 16, 32], "warning": [16, 16, 32, 32],
    "cloth": [32, 16, 48, 32], "blue": [48, 16, 64, 32]
}

def cube(a, b, material="metal"):
    faces = {face: {"uv": UV[material], "texture": "#atlas"} for face in ("north","south","east","west","up","down")}
    return {"from": a, "to": b, "faces": faces}

DISPLAY = {
    "gui": {"rotation":[25,-135,0], "translation":[0,0,0], "scale":[0.88,0.88,0.88]},
    "ground": {"rotation":[0,0,0], "translation":[0,2,0], "scale":[0.48,0.48,0.48]},
    "fixed": {"rotation":[0,180,0], "translation":[0,0,0], "scale":[0.72,0.72,0.72]},
    "thirdperson_righthand":{"rotation":[0,-90,55],"translation":[0,2.5,0],"scale":[0.72,0.72,0.72]},
    "thirdperson_lefthand":{"rotation":[0,90,-55],"translation":[0,2.5,0],"scale":[0.72,0.72,0.72]},
    "firstperson_righthand":{"rotation":[0,-90,25],"translation":[1.1,3.1,1.1],"scale":[0.92,0.92,0.92]},
    "firstperson_lefthand":{"rotation":[0,90,-25],"translation":[1.1,3.1,1.1],"scale":[0.92,0.92,0.92]}
}

def write(name, elements):
    ROOT.mkdir(parents=True, exist_ok=True)
    data = {"credit":"Original WinterFall low-poly model", "textures":{"atlas":ATLAS,"particle":ATLAS},
            "display":DISPLAY, "elements":elements}
    (ROOT / f"{name}.json").write_text(json.dumps(data, indent=2) + "\n")

# Melee: blade / edge / guard / grip / pommel have real thickness in separate cuboids.
write("combat_knife", [
    cube([1,7,7],[14,9,9],"metal"), cube([13,7.3,7.2],[16,8.7,8.8],"metal"),
    cube([2,7.1,6.7],[14,7.55,7.1],"blue"), cube([0,5.5,6.2],[2,10.5,9.8],"dark"),
    cube([0,6,6.6],[6,10,9.4],"grip"), cube([5,5.4,6.2],[7,10.6,9.8],"dark")
])
write("billhook", [
    cube([1,6.5,6.5],[13,9.5,9.5],"wood"), cube([12,6,6],[15,10,10],"metal"),
    cube([14,7,7],[17,9,9],"metal"), cube([16,8,7],[18.5,11,9],"metal"),
    cube([17.5,10,7],[19,14,9],"metal"), cube([11,6.6,6.6],[12,9.4,9.4],"warning")
])
write("pipe_wrench", [
    cube([2,6.5,6.5],[14,9.5,9.5],"metal"), cube([0,5,5.4],[5,11,10.6],"metal"),
    cube([0,7,4.8],[2,9,11.2],"dark"), cube([13,5.5,5.8],[16,10.5,10.2],"rust")
])
write("cleaver", [
    cube([1,5,6.5],[12,11,9.5],"metal"), cube([2,5.2,6.2],[12,5.7,6.5],"blue"),
    cube([11,6,6.4],[17,10,9.6],"grip"), cube([16,5.5,6],[18,10.5,10],"dark")
])
write("machete", [
    cube([1,6.5,6.7],[16,9.5,9.3],"metal"), cube([3,6.7,6.4],[15,7.2,6.7],"blue"),
    cube([15,5.8,6.2],[21,10.2,9.8],"grip"), cube([20,5.4,6],[22,10.6,10],"dark")
])
write("hatchet", [
    cube([7,6.5,6.5],[17,9.5,9.5],"wood"), cube([1,3.5,5.5],[9,11.5,10.5],"metal"),
    cube([2,4,5.2],[8,6,5.5],"blue"), cube([15,5.8,6],[19,10.2,10],"grip")
])
write("sledgehammer", [
    cube([8,6.7,6.7],[22,9.3,9.3],"wood"), cube([1,4,5],[10,12,11],"dark"),
    cube([2,4.5,5.3],[9,7,10.7],"metal"), cube([20,5.8,6],[24,10.2,10],"grip")
])
write("crowbar", [
    cube([2,6.8,6.8],[20,9.2,9.2],"rust"), cube([0,7,6.3],[3,10,9.7],"rust"),
    cube([19,7,6.2],[22,12,9.8],"rust"), cube([7,6.5,6.5],[14,9.5,9.5],"warning")
])
write("scrap_spear", [
    cube([1,7,7],[22,9,9],"wood"), cube([20,5,5.5],[29,11,10.5],"metal"),
    cube([26,6,6],[31,10,10],"metal"), cube([5,6.5,6.5],[10,9.5,9.5],"cloth")
])
write("riot_baton", [
    cube([3,6,6],[20,10,10],"dark"), cube([2,5,5],[7,11,11],"metal"),
    cube([9,5.5,5.5],[16,10.5,10.5],"blue"), cube([18,5.5,5.5],[23,10.5,10.5],"grip")
])
write("trench_shovel", [
    cube([3,6.8,6.8],[18,9.2,9.2],"wood"), cube([17,4.5,5.5],[29,11.5,10.5],"metal"),
    cube([20,4.8,5.2],[28,6.2,5.5],"blue"), cube([1,5.8,6],[6,10.2,10],"grip")
])

# Firearms: each has its own receiver/barrel/grip/magazine/stock language.
write("broomhandle_22", [
    cube([3,6,6],[18,10,10],"metal"), cube([4,4.8,6.4],[13,6,9.6],"blue"),
    cube([17,7,7],[27,9,9],"dark"), cube([11,9,6],[15,16,10],"wood"),
    cube([7,10,6.5],[11,13,9.5],"grip"), cube([15,5.5,6.5],[17,6,9.5],"warning")
])
write("mark_vii", [
    cube([3,5.5,5.5],[20,11,10.5],"dark"), cube([5,5.8,5.2],[18,7,5.5],"metal"),
    cube([19,7,7],[28,9,9],"metal"), cube([10,10,5.8],[16,18,10.2],"grip"),
    cube([9,9.5,5.4],[17,11,5.8],"warning"), cube([6,7,5.3],[9,9,5.6],"blue")
])
write("service_rifle", [
    cube([2,6.5,6.5],[24,9.5,9.5],"dark"), cube([23,7,7],[31,9,9],"metal"),
    cube([2,7,5],[10,10,11],"wood"), cube([11,9,5.8],[15,16,10.2],"grip"),
    cube([15,8.5,5.8],[19,13,10.2],"metal"), cube([7,4.5,6],[15,6.5,10],"olive" if False else "cloth")
])
write("pump_shotgun", [
    cube([2,6.5,6.2],[27,9.5,9.8],"metal"), cube([23,6,6],[31,8,10],"dark"),
    cube([3,7,5],[12,10,11],"wood"), cube([12,9,5.8],[16,17,10.2],"grip"),
    cube([17,9,5.5],[24,12,10.5],"wood"), cube([6,5.8,6.4],[15,6.5,9.6],"blue")
])
write("improvised_smg", [
    cube([4,5.5,5.5],[20,11,10.5],"rust"), cube([18,7,7],[28,9,9],"metal"),
    cube([8,10,5.8],[13,17,10.2],"grip"), cube([14,10,6],[18,15,10],"dark"),
    cube([3,6,5.2],[8,8,5.5],"warning"), cube([18,4,6],[22,5.5,10],"metal")
])
write("hunting_rifle", [
    cube([1,7,6.5],[29,9,9.5],"metal"), cube([2,8,5],[20,11,11],"wood"),
    cube([28,7.2,7],[31,8.8,9],"dark"), cube([12,9,5.8],[16,17,10.2],"grip"),
    cube([10,4.5,6],[19,6.5,10],"dark"), cube([4,6.5,5.3],[7,7,5.6],"blue")
])
write("flare_gun", [
    cube([5,6,6],[18,11,10],"warning"), cube([17,7,7],[25,9,9],"red" if False else "metal"),
    cube([10,10,5.8],[15,18,10.2],"grip"), cube([6,5.5,5.5],[10,7,10.5],"warning"),
    cube([4,7,5.2],[7,9,5.5],"blue")
])
