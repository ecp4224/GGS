package net.mcforge.world.blocks.convert;

import net.mcforge.API.level.BlockConvertEvent;
import net.mcforge.server.Server;

public class OldBlocks {
    
    private static final byte air = (byte)0;
    private static final byte rock = (byte)1;
    private static final byte grass = (byte)2;
    private static final byte dirt = (byte)3;
    private static final byte stone = (byte)4;
    private static final byte wood = (byte)5;
    private static final byte water = (byte)8;
    private static final byte waterstill = (byte)9;
    private static final byte lava = (byte)10;
    private static final byte lavastill = (byte)11;
    private static final byte sand = (byte)12;
    private static final byte coal = (byte)16;
    private static final byte trunk = (byte)17;
    private static final byte leaf = (byte)18;
    private static final byte sponge = (byte)19;
    private static final byte glass = (byte)20;
    private static final byte red = (byte)21;
    private static final byte orange = (byte)22;
    private static final byte yellow = (byte)23;
    private static final byte lightgreen = (byte)24;
    private static final byte green = (byte)25;
    private static final byte aquagreen = (byte)26;
    private static final byte cyan = (byte)27;
    private static final byte lightblue = (byte)28;
    private static final byte blue = (byte)29;
    private static final byte purple = (byte)30;
    private static final byte lightpurple = (byte)31;
    private static final byte pink = (byte)32;
    private static final byte darkpink = (byte)33;
    private static final byte darkgrey = (byte)34;
    private static final byte lightgrey = (byte)35;
    private static final byte white = (byte)36;
    private static final byte mushroom = (byte)39;
    private static final byte goldsolid = (byte)41;
    private static final byte iron = (byte)42;
    private static final byte staircasestep = (byte)44;
    private static final byte tnt = (byte)46;
    private static final byte bookcase = (byte)47;
    private static final byte stonevine = (byte)48;
    private static final byte obsidian = (byte)49;
    //Custom s
    private static final byte door_orange_air = (byte)57;
    private static final byte door_yellow_air = (byte)58;
    private static final byte door_lightgreen_air = (byte)59;
    private static final byte door_aquagreen_air = (byte)60;
    private static final byte door_cyan_air = (byte)61;
    private static final byte door_lightblue_air = (byte)62;
    private static final byte door_purple_air = (byte)63;
    private static final byte door_lightpurple_air = (byte)64;
    private static final byte door_pink_air = (byte)65;
    private static final byte door_darkpink_air = (byte)66;
    private static final byte door_darkgrey_air = (byte)67;
    private static final byte door_lightgrey_air = (byte)68;
    private static final byte door_white_air = (byte)69;

    private static final byte flagbase = (byte)70;

    private static final byte fastdeathlava = (byte)73;

    private static final byte c4 = (byte)74;
    private static final byte c4det = (byte)75;

    private static final byte door_cobblestone = (byte)80;
    private static final byte door_cobblestone_air = (byte)81;
    private static final byte door_red = (byte)83;
    private static final byte door_red_air = (byte)84;

    private static final byte door_orange = (byte)85;
    private static final byte door_yellow = (byte)86;
    private static final byte door_lightgreen = (byte)87;
    private static final byte door_aquagreen = (byte)89;
    private static final byte door_cyan = (byte)90;
    private static final byte door_lightblue = (byte)91;
    private static final byte door_purple = (byte)92;
    private static final byte door_lightpurple = (byte)93;
    private static final byte door_pink = (byte)94;
    private static final byte door_darkpink = (byte)95;
    private static final byte door_darkgrey = (byte)96;
    private static final byte door_lightgrey = (byte)97;
    private static final byte door_white = (byte)98;

    private static final byte door = (byte)111;
    private static final byte door2 = (byte)113;
    private static final byte door3 = (byte)114;
    private static final byte door4 = (byte)115;
    private static final byte door5 = (byte)116;
    private static final byte door6 = (byte)117;
    private static final byte door7 = (byte)118;
    private static final byte door8 = (byte)119;
    private static final byte door9 = (byte)120;
    private static final byte door10 = (byte)121;

    private static final byte tdoor = (byte)122;
    private static final byte tdoor2 = (byte)123;
    private static final byte tdoor3 = (byte)124;
    private static final byte tdoor4 = (byte)125;
    private static final byte tdoor5 = (byte)126;
    private static final byte tdoor6 = (byte)127;
    private static final byte tdoor7 = (byte)128;
    private static final byte tdoor8 = (byte)129;

    private static final byte MsgWater = (byte)133;
    private static final byte MsgLava = (byte)134;

    private static final byte tdoor9 = (byte)135;
    private static final byte tdoor10 = (byte)136;
    private static final byte tdoor11 = (byte)137;
    private static final byte tdoor12 = (byte)138;
    private static final byte tdoor13 = (byte)139;

    private static final byte WaterFaucet = (byte)143;
    private static final byte LavaFaucet = (byte)144;

    private static final byte finiteWater = (byte)145;
    private static final byte finiteLava = (byte)146;
    private static final byte finiteFaucet = (byte)147;

    private static final byte odoor1 = (byte)148;
    private static final byte odoor2 = (byte)149;
    private static final byte odoor3 = (byte)150;
    private static final byte odoor4 = (byte)151;
    private static final byte odoor5 = (byte)152;
    private static final byte odoor6 = (byte)153;
    private static final byte odoor7 = (byte)154;
    private static final byte odoor8 = (byte)155;
    private static final byte odoor9 = (byte)156;
    private static final byte odoor10 = (byte)157;
    private static final byte odoor11 = (byte)158;
    private static final byte odoor12 = (byte)159;

    //Movement doors
    private static final byte air_door = (byte)164;
    private static final byte air_switch = (byte)165;
    private static final byte water_door = (byte)166;
    private static final byte lava_door = (byte)167;

    private static final byte odoor1_air = (byte)168;
    private static final byte odoor2_air = (byte)169;
    private static final byte odoor3_air = (byte)170;
    private static final byte odoor4_air = (byte)171;
    private static final byte odoor5_air = (byte)172;
    private static final byte odoor6_air = (byte)173;
    private static final byte odoor7_air = (byte)174;

    private static final byte odoor8_air = (byte)177;
    private static final byte odoor9_air = (byte)178;
    private static final byte odoor10_air = (byte)179;
    private static final byte odoor11_air = (byte)180;
    private static final byte odoor12_air = (byte)181;

    private static final byte fire = (byte)185;
    
    private static final byte rocketstart = (byte)187;
    private static final byte rockethead = (byte)188;
    private static final byte firework = (byte)189;

    //Death
    private static final byte deathlava = (byte)190;
    private static final byte deathwater = (byte)191;
    private static final byte deathair = (byte)192;

    private static final byte activedeathwater = (byte)193;
    private static final byte activedeathlava = (byte)194;

    private static final byte magma = (byte)195;
    private static final byte geyser = (byte)196;

    private static final byte door8_air = (byte)211;
    private static final byte door9_air = (byte)212;
    private static final byte door14_air = (byte)217;

    private static final byte door_iron = (byte)220;
    private static final byte door_dirt = (byte)221;
    private static final byte door_grass = (byte)222;
    private static final byte door_blue = (byte)223;
    private static final byte door_book = (byte)224;
    private static final byte door_iron_air = (byte)225;
    private static final byte door_dirt_air = (byte)226;
    private static final byte door_grass_air = (byte)227;
    private static final byte door_blue_air = (byte)228;
    private static final byte door_book_air = (byte)229;

    private static final byte train = (byte)230;

    private static final byte creeper = (byte)231;
    private static final byte zombiebody = (byte)232;
    private static final byte zombiehead = (byte)233;

    private static final byte birdwhite = (byte)235;
    private static final byte birdblack = (byte)236;
    private static final byte birdwater = (byte)237;
    private static final byte birdlava = (byte)238;
    private static final byte birdred = (byte)239;
    private static final byte birdblue = (byte)240;
    private static final byte birdkill = (byte)242;

    private static final byte fishgold = (byte)245;
    private static final byte fishsponge = (byte)246;
    private static final byte fishshark = (byte)247;
    private static final byte fishsalmon = (byte)248;
    private static final byte fishbetta = (byte)249;
    private static final byte fishlavashark = (byte)250;

    private static final byte snake = (byte)251;
    private static final byte snaketail = (byte)252;
    
    private static final byte door_gold = (byte)253;
    private static final byte door_gold_air = (byte)254;
    
    public static byte convert(byte b, Server s)
    {
        if (s != null) {
            BlockConvertEvent bce = new BlockConvertEvent(b);
            s.getEventSystem().callEvent(bce);
            if (bce.isCancelled())
                return bce.getNewBlock();
        }
        switch (b)
        {
            case flagbase: return mushroom; //CTF Flagbase
            case 100: return (byte)20; //Op_glass
            case 101: return (byte)49; //Opsidian
            case 102: return (byte)45; //Op_brick
            case 103: return (byte)1; //Op_stone
            case 104: return (byte)4; //Op_cobblestone
            case 105: return (byte)0; //Op_air - Must be cuboided / replaced
            case 106: return waterstill; //Op_water
            case 107: return lavastill; //Op_lava

            case 108: return stone; //Griefer_stone
            case 109: return (byte)19; //Lava_sponge

            case 110: return (byte)5; //wood_float
            case 112: return (byte)10;
            case 71:
            case 72:
                return white;
            case door: return trunk;//door show by treetype
            case door2: return obsidian;//door show by obsidian
            case door3: return glass;//door show by glass
            case door4: return rock;//door show by stone
            case door5: return leaf;//door show by leaves
            case door6: return sand;//door show by sand
            case door7: return wood;//door show by wood
            case door8: return green;
            case door9: return tnt;//door show by TNT
            case door10: return staircasestep;//door show by Stair
            case door_iron: return iron;
            case door_dirt: return dirt;
            case door_grass: return grass;
            case door_blue: return blue;
            case door_book: return bookcase;
            case door_gold: return goldsolid;
            case door_cobblestone: return 4;
            case door_red: return red;

            case door_orange: return orange;
            case door_yellow: return yellow;
            case door_lightgreen: return lightgreen;
            case door_aquagreen: return aquagreen;
            case door_cyan: return cyan;
            case door_lightblue: return lightblue;
            case door_purple: return purple;
            case door_lightpurple: return lightpurple;
            case door_pink: return pink;
            case door_darkpink: return darkpink;
            case door_darkgrey: return darkgrey;
            case door_lightgrey: return lightgrey;
            case door_white: return white;

            case tdoor: return trunk;//tdoor show by treetype
            case tdoor2: return obsidian;//tdoor show by obsidian
            case tdoor3: return glass;//tdoor show by glass
            case tdoor4: return rock;//tdoor show by stone
            case tdoor5: return leaf;//tdoor show by leaves
            case tdoor6: return sand;//tdoor show by sand
            case tdoor7: return wood;//tdoor show by wood
            case tdoor8: return green;
            case tdoor9: return tnt;//tdoor show by TNT
            case tdoor10: return staircasestep;//tdoor show by Stair
            case tdoor11: return air;
            case tdoor12: return waterstill;
            case tdoor13: return lavastill;

            case odoor1: return trunk;//odoor show by treetype
            case odoor2: return obsidian;//odoor show by obsidian
            case odoor3: return glass;//odoor show by glass
            case odoor4: return rock;//odoor show by stone
            case odoor5: return leaf;//odoor show by leaves
            case odoor6: return sand;//odoor show by sand
            case odoor7: return wood;//odoor show by wood
            case odoor8: return green;
            case odoor9: return tnt;//odoor show by TNT
            case odoor10: return staircasestep;//odoor show by Stair
            case odoor11: return lavastill;
            case odoor12: return waterstill;

            case (byte) 130: return (byte)36;  //upVator
            case (byte) 131: return (byte)34;  //upVator
            case (byte) 132: return (byte)0;   //upVator
            case MsgWater: return waterstill;   //upVator
            case MsgLava: return lavastill;  //upVator

            case (byte) 140: return (byte)8;
            case (byte) 141: return (byte)10;
            case WaterFaucet: return cyan;
            case LavaFaucet: return orange;

            case finiteWater: return water;
            case finiteLava: return lava;
            case finiteFaucet: return lightblue;

            case (byte) 160: return (byte)0;//air portal
            case (byte) 161: return waterstill;//water portal
            case (byte) 162: return lavastill;//lava portal

            case air_door: return air;
            case air_switch: return air;//air door
            case water_door: return waterstill;//water door
            case lava_door: return lavastill;

            case (byte) 175: return (byte)28;//blue portal
            case (byte) 176: return (byte)22;//orange portal

            case c4: return (byte)46;
            case c4det: return (byte)red;
            case (byte) 182: return (byte)46;//smalltnt
            case (byte) 183: return (byte)46;//bigtnt
            case (byte) 186: return (byte)46;//nuketnt
            case (byte) 184: return (byte)10;//explosion

            case fire: return lava;

            case rocketstart: return glass;
            case rockethead: return goldsolid;
            case firework: return iron;

            case deathwater: return waterstill;
            case deathlava: return lavastill;
            case deathair: return (byte)0;
            case activedeathwater: return water;
            case activedeathlava: return lava;
            case fastdeathlava: return lava;

            case magma: return lava;
            case geyser: return water;

            case (byte) 200: //air_flood
            case (byte) 201: //door_air
            case (byte) 202: //air_flood_layer
            case (byte) 203: //air_flood_down
            case (byte) 204: //air_flood_up
            case (byte) 205: //door2_air
            case (byte) 206: //door3_air
            case (byte) 207: //door4_air
            case (byte) 208: //door5_air
            case (byte) 209: //door6_air
            case (byte) 210: //door7_air
            case (byte) 213: //door10_air
            case (byte) 214: //door10_air
            case (byte) 215: //door10_air
            case (byte) 216: //door10_air
            case door14_air:
            case door_iron_air:
            case door_gold_air:
            case door_cobblestone_air:
            case door_red_air: 
            case door_dirt_air:
            case door_grass_air:
            case door_blue_air:
            case door_book_air:
                return (byte)0;
            case door9_air: return lava;
            case door8_air: return red;

            case door_orange_air:
            case door_yellow_air:
            case door_lightgreen_air:
            case door_aquagreen_air:
            case door_cyan_air:
            case door_lightblue_air:
            case door_purple_air:
            case door_lightpurple_air:
            case door_pink_air:
            case door_darkpink_air:
            case door_darkgrey_air:
            case door_lightgrey_air:
            case door_white_air:

            case odoor1_air:
            case odoor2_air:
            case odoor3_air:
            case odoor4_air:
            case odoor5_air:
            case odoor6_air:
            case odoor7_air:
            case odoor10_air:
            case odoor11_air:
            case odoor12_air:
                return air;
            case odoor8_air: return red;
            case odoor9_air: return lavastill;

            case train: return cyan;

            case snake: return darkgrey;
            case snaketail: return coal;

            case creeper: return tnt;
            case zombiebody: return stonevine;
            case zombiehead: return lightgreen;

            case birdwhite: return white;
            case birdblack: return darkgrey;
            case birdlava: return lava;
            case birdred: return red;
            case birdwater: return water;
            case birdblue: return blue;
            case birdkill: return lava;

            case fishbetta: return blue;
            case fishgold: return goldsolid;
            case fishsalmon: return red;
            case fishshark: return lightgrey;
            case fishsponge: return sponge;
            case fishlavashark: return obsidian;

            default:
                if (b < 50) return b; else return 22;
        }
    }
}
