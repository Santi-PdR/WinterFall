"""Original WinterFall pixel-art asset generator. No external game assets are used."""
from pathlib import Path
import json, struct, zlib

ROOT = Path(__file__).parents[1] / "src/main/resources/assets/winterfall"
W = H = 32
T = (0, 0, 0, 0)
OUT, INK, STEEL, HILITE, GUN, OLIVE, LEATHER, RUST, RED, BRASS, PAPER, ICE = (
    (18, 24, 29, 255), (28, 35, 39, 255), (105, 123, 130, 255), (193, 213, 213, 255),
    (51, 60, 64, 255), (69, 82, 58, 255), (105, 72, 48, 255), (141, 71, 41, 255),
    (164, 42, 45, 255), (205, 161, 67, 255), (109, 145, 161, 255), (114, 177, 198, 255)
)

ITEMS = ["short_ammo","long_ammo","shotgun_shell","billhook_blueprint","field_ration","canteen",
         "combat_knife","billhook","pipe_wrench","cleaver","machete","hatchet","sledgehammer",
         "crowbar","scrap_spear","riot_baton","trench_shovel","broomhandle_22","mark_vii",
         "service_rifle","pump_shotgun","improvised_smg","hunting_rifle","flare_gun",
         "combat_knife_scavenger_spawn_egg"]
MELEE = {"combat_knife","billhook","pipe_wrench","cleaver","machete","hatchet","sledgehammer","crowbar","scrap_spear","riot_baton","trench_shovel"}

def png(path, p, w=W, h=H):
    raw = b"".join(b"\0" + b"".join(bytes(px) for px in row) for row in p)
    def c(tag, data): return struct.pack(">I",len(data))+tag+data+struct.pack(">I",zlib.crc32(tag+data)&0xffffffff)
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(b"\x89PNG\r\n\x1a\n"+c(b"IHDR",struct.pack(">IIBBBBB",w,h,8,6,0,0,0))+c(b"IDAT",zlib.compress(raw,9))+c(b"IEND",b""))

def canvas(): return [[T for _ in range(W)] for _ in range(H)]
def px(p,x,y,c):
    if 0 <= x < W and 0 <= y < H: p[y][x] = c
def rect(p,x,y,w,h,c):
    for yy in range(y,y+h):
        for xx in range(x,x+w): px(p,xx,yy,c)
def line(p,x0,y0,x1,y1,c,thick=1):
    dx, sx, dy, sy = abs(x1-x0), 1 if x0<x1 else -1, -abs(y1-y0), 1 if y0<y1 else -1
    err = dx+dy
    while True:
        for ox in range(-(thick//2),thick//2+1):
            for oy in range(-(thick//2),thick//2+1): px(p,x0+ox,y0+oy,c)
        if x0==x1 and y0==y1: break
        e2=2*err
        if e2>=dy: err+=dy; x0+=sx
        if e2<=dx: err+=dx; y0+=sy
def rivets(p, points):
    for x,y in points: px(p,x,y,HILITE); px(p,x+1,y+1,INK)
def outline_rect(p,x,y,w,h,fill):
    rect(p,x-1,y-1,w+2,h+2,OUT); rect(p,x,y,w,h,fill)

def bullets(p, long=False):
    for x in ([8,16,24] if long else [10,19]):
        rect(p,x,10 if long else 13,3,14 if long else 11,BRASS)
        rect(p,x,8 if long else 11,3,3,STEEL if long else BRASS)
        rect(p,x,22 if long else 22,3,2,INK)
        px(p,x+1,9 if long else 12,HILITE)

def blade(p, x0,y0,x1,y1, handle=True, hook=False):
    line(p,x0,y0,x1,y1,OUT,4); line(p,x0,y0,x1,y1,STEEL,2)
    line(p,x0-1,y0,x1-1,y1,HILITE,1)
    if handle:
        line(p,x1,y1,x1+5,y1+5,OUT,5); line(p,x1,y1,x1+5,y1+5,LEATHER,3)
        rivets(p,[(x1+2,y1+3),(x1+4,y1+5)])
    if hook:
        line(p,x0,y0,x0+4,y0-1,STEEL,3); line(p,x0+4,y0-1,x0+6,y0+3,STEEL,3)

def gun(p, kind):
    # Shared dark receiver, then each profile gets a distinct silhouette and finish.
    if kind == "broomhandle_22":
        rect(p,6,13,17,5,GUN); rect(p,8,11,10,2,STEEL); rect(p,19,14,5,2,BRASS)
        line(p,13,17,16,25,LEATHER,4); rect(p,8,15,2,2,HILITE)
    elif kind == "mark_vii":
        rect(p,7,12,18,7,GUN); rect(p,10,10,12,2,STEEL); rect(p,22,14,4,2,HILITE)
        line(p,15,18,18,26,INK,5); line(p,14,18,17,26,LEATHER,3); rect(p,9,13,2,4,RED)
    elif kind == "service_rifle":
        rect(p,4,13,23,4,GUN); rect(p,23,12,6,2,STEEL); rect(p,7,10,8,3,OLIVE)
        line(p,14,17,16,25,LEATHER,4); rect(p,18,16,6,3,STEEL); rect(p,5,17,5,2,LEATHER)
    elif kind == "pump_shotgun":
        rect(p,3,13,25,4,OUT); rect(p,4,13,24,3,STEEL); rect(p,19,16,7,3,LEATHER)
        line(p,13,16,15,25,LEATHER,4); rect(p,5,12,18,1,HILITE); rivets(p,[(7,15),(25,15)])
    elif kind == "improvised_smg":
        rect(p,6,12,17,7,OUT); rect(p,7,12,15,6,RUST); rect(p,19,14,7,2,STEEL)
        line(p,13,18,14,25,INK,5); line(p,11,18,13,24,LEATHER,3); rect(p,8,10,5,2,STEEL)
    elif kind == "hunting_rifle":
        rect(p,3,13,26,3,OUT); rect(p,4,13,25,2,STEEL); rect(p,6,16,18,3,LEATHER)
        rect(p,11,10,8,2,GUN); line(p,16,18,18,25,LEATHER,4); px(p,27,14,HILITE)
    elif kind == "flare_gun":
        rect(p,9,12,13,6,RED); rect(p,20,13,5,3,BRASS); line(p,14,18,16,25,INK,5)
        line(p,13,18,15,25,LEATHER,3); rect(p,10,13,3,2,HILITE)

def item(name):
    p=canvas()
    if name=="short_ammo": bullets(p)
    elif name=="long_ammo": bullets(p, True)
    elif name=="shotgun_shell":
        outline_rect(p,13,8,6,17,RED); rect(p,13,20,6,4,BRASS); rect(p,14,9,4,2,HILITE); rect(p,14,13,4,5,(114,39,41,255))
    elif name=="billhook_blueprint":
        outline_rect(p,5,5,22,22,PAPER)
        for x in range(8,25,5): line(p,x,7,x,25,(72,109,128,255))
        for y in range(9,25,5): line(p,7,y,25,y,(72,109,128,255))
        blade(p,11,20,20,11,False,True); rect(p,7,22,7,2,RED)
    elif name=="field_ration":
        outline_rect(p,7,9,18,14,OLIVE); rect(p,8,11,16,4,(116,127,72,255)); rect(p,10,16,12,5,LEATHER); rect(p,12,17,8,1,BRASS); rivets(p,[(9,10),(23,10),(9,21),(23,21)])
    elif name=="canteen":
        outline_rect(p,10,7,12,19,OLIVE); rect(p,13,5,6,3,STEEL); rect(p,12,10,8,10,(89,108,72,255)); line(p,10,9,8,14,LEATHER,2); line(p,22,9,24,14,LEATHER,2); rect(p,13,21,6,2,INK)
    elif name=="combat_knife": blade(p,8,8,21,21)
    elif name=="billhook": blade(p,9,16,20,8,True,True)
    elif name=="pipe_wrench":
        line(p,9,8,22,23,OUT,5); line(p,9,8,22,23,STEEL,3); rect(p,6,6,7,5,STEEL); rect(p,6,6,4,2,HILITE); rect(p,19,21,5,4,RUST)
    elif name=="cleaver":
        rect(p,8,7,12,13,STEEL); rect(p,7,8,2,12,HILITE); line(p,18,19,23,25,LEATHER,5); rivets(p,[(20,21),(22,23)])
    elif name=="machete": blade(p,7,9,23,19); line(p,9,10,22,18,HILITE)
    elif name=="hatchet":
        line(p,12,9,22,24,LEATHER,4); rect(p,7,7,10,7,STEEL); rect(p,8,8,7,2,HILITE); rect(p,15,8,3,5,INK)
    elif name=="sledgehammer":
        line(p,10,7,23,25,LEATHER,4); rect(p,5,6,13,6,STEEL); rect(p,6,7,11,2,HILITE); rivets(p,[(8,9),(15,9)])
    elif name=="crowbar":
        line(p,8,7,22,24,OUT,4); line(p,8,7,22,24,RUST,2); line(p,8,7,12,6,RUST,2); line(p,22,24,25,20,RUST,2)
    elif name=="scrap_spear":
        line(p,6,26,25,6,LEATHER,3); line(p,20,10,26,4,OUT,5); line(p,20,10,26,4,STEEL,3); rect(p,10,20,5,2,OLIVE)
    elif name=="riot_baton":
        line(p,12,7,21,25,OUT,6); line(p,12,7,21,25,GUN,4); rect(p,10,6,5,5,STEEL); rect(p,17,17,4,5,ICE); rect(p,19,23,4,2,LEATHER)
    elif name=="trench_shovel":
        line(p,13,7,21,22,LEATHER,4); rect(p,18,20,8,7,STEEL); rect(p,19,21,6,4,HILITE); rect(p,12,8,3,5,INK)
    elif name in {"broomhandle_22","mark_vii","service_rifle","pump_shotgun","improvised_smg","hunting_rifle","flare_gun"}: gun(p,name)
    elif name=="combat_knife_scavenger_spawn_egg":
        outline_rect(p,9,7,14,18,(53,74,78,255)); rect(p,11,9,10,5,(105,132,135,255)); rect(p,13,11,6,2,RED); rect(p,11,16,10,6,OLIVE); px(p,12,19,HILITE); px(p,20,20,HILITE)
    return p

def block_scrapper(side, top=False):
    p=[[OUT for _ in range(W)] for _ in range(H)]
    rect(p,2,2,28,28,INK)
    if top:
        rect(p,4,4,24,24,GUN); rect(p,7,7,18,18,STEEL); rect(p,10,10,12,12,INK); rect(p,13,8,6,16,RUST); rect(p,8,13,16,6,RUST)
        rivets(p,[(5,5),(26,5),(5,26),(26,26),(9,9),(23,9),(9,23),(23,23)])
    else:
        rect(p,4,4,24,24,GUN); rect(p,6,6,20,4,STEEL); rect(p,8,12,16,12,INK); rect(p,10,14,12,7,(48,57,60,255))
        for x in range(11,22,4): rect(p,x,15,2,5,RUST)
        line(p,7,25,25,25,STEEL,2); rivets(p,[(5,5),(26,5),(5,26),(26,26)])
    return p

def block_workbench(top=False):
    p=[[OUT for _ in range(W)] for _ in range(H)]
    if top:
        rect(p,2,2,28,28,LEATHER)
        for y in (5,13,21): line(p,3,y,28,y,(64,42,31,255),2)
        rect(p,9,7,14,16,PAPER); line(p,11,10,21,10,ICE); line(p,11,14,21,14,ICE); line(p,11,18,18,18,ICE); rect(p,17,19,4,2,RED)
        rivets(p,[(4,4),(27,4),(4,27),(27,27)])
    else:
        rect(p,3,3,26,26,(69,48,36,255)); rect(p,5,5,22,5,LEATHER); rect(p,5,12,22,14,(54,42,35,255))
        line(p,9,12,9,27,STEEL,2); line(p,23,12,23,27,STEEL,2); line(p,5,23,27,23,STEEL,2); rivets(p,[(6,6),(26,6),(6,25),(26,25)])
    return p

def model(name):
    parent = "minecraft:item/handheld" if name in MELEE else "minecraft:item/generated"
    return {"parent":parent,"textures":{"layer0":f"winterfall:item/{name}"},
            "display":{"firstperson_righthand":{"rotation":[0,-90,25],"translation":[1.13,3.2,1.13],"scale":[0.82,0.82,0.82]},
                       "thirdperson_righthand":{"rotation":[0,-90,55],"translation":[0,2.5,0],"scale":[0.7,0.7,0.7]},
                       "ground":{"rotation":[0,0,0],"translation":[0,2,0],"scale":[0.5,0.5,0.5]}}}

for name in ITEMS:
    png(ROOT/"textures/item"/f"{name}.png", item(name))
    (ROOT/"models/item"/f"{name}.json").write_text(json.dumps(model(name),indent=2)+"\n")

png(ROOT/"textures/block"/"scrapper.png", block_scrapper(True))
png(ROOT/"textures/block"/"scrapper_top.png", block_scrapper(True,True))
png(ROOT/"textures/block"/"scrapper_bottom.png", block_scrapper(True))
png(ROOT/"textures/block"/"workbench.png", block_workbench())
png(ROOT/"textures/block"/"workbench_top.png", block_workbench(True))

# 64px standard humanoid UV skin with a worn steel mask, blue-grey coat, red scarf, harness, gloves and boots.
mob=[[(42,49,52,255) for _ in range(64)] for _ in range(64)]
def mrect(x,y,w,h,c): 
    for yy in range(y,y+h):
        for xx in range(x,x+w):
            if 0<=xx<64 and 0<=yy<64: mob[yy][xx]=c
# face UV (front), mask plate / red visor / hood
mrect(8,8,8,8,(78,92,95,255)); mrect(9,9,6,2,STEEL); mrect(9,12,6,2,(120,29,31,255)); mrect(10,13,4,1,HILITE)
mrect(0,8,8,8,(34,42,46,255)); mrect(16,8,8,8,(34,42,46,255)); mrect(24,8,8,8,(47,56,60,255))
# body front and side, military coat, harness, scarf
mrect(20,20,8,12,(55,76,86,255)); mrect(21,20,6,2,RED); mrect(23,22,2,10,(31,35,37,255)); mrect(20,25,8,2,LEATHER); mrect(21,28,6,3,(71,92,97,255))
mrect(16,20,4,12,(45,62,70,255)); mrect(28,20,4,12,(45,62,70,255)); mrect(20,16,8,4,(35,43,47,255))
# arms, gloves and boots
for x in (44, 4):
    mrect(x,20,4,12,(48,67,75,255)); mrect(x,29,4,3,LEATHER); mrect(x,32,4,4,(33,39,42,255))
mrect(4,52,4,12,(33,41,45,255)); mrect(20,52,4,12,(33,41,45,255)); mrect(4,60,4,4,INK); mrect(20,60,4,4,INK)
for y in range(64):
    for x in range(64):
        if (x*7+y*11)%17==0: mob[y][x]=(31,37,40,255)
png(ROOT/"textures/entity/combat_knife_scavenger.png",mob,64,64)

# 64px atlas dedicated to low-poly weapon faces. Each tile has a deliberate highlight/mid/shadow treatment.
atlas = [[(24,29,33,255) for _ in range(64)] for _ in range(64)]
tiles = [
    ((0,0),(96,116,124),(151,178,183),(47,57,63)),      # cold steel
    ((16,0),(47,57,62),(77,90,94),(25,31,35)),           # gunmetal
    ((32,0),(105,72,48),(145,104,70),(56,39,30)),        # leather grip
    ((48,0),(103,71,49),(149,105,73),(57,39,30)),        # worn wood
    ((0,16),(132,68,43),(174,92,54),(65,38,30)),         # rust
    ((16,16),(155,54,42),(213,121,50),(69,38,29)),       # warning
    ((32,16),(62,82,89),(91,115,121),(32,43,47)),        # cloth
    ((48,16),(79,145,164),(145,203,211),(35,76,89)),     # cold blue paint
]
for (ox,oy), mid, hi, shadow in tiles:
    for y in range(16):
        for x in range(16):
            atlas[oy+y][ox+x] = mid
            if x in (0,1) or y in (0,1): atlas[oy+y][ox+x] = hi
            if x in (14,15) or y in (14,15): atlas[oy+y][ox+x] = shadow
            if (x*5+y*3+ox+oy) % 17 == 0: atlas[oy+y][ox+x] = shadow
            if (x+y) % 13 == 0: atlas[oy+y][ox+x] = hi
png(ROOT/"textures/item/weapon_atlas.png",atlas,64,64)
