#version 150
in vec2 inPosition;// input from the vertex buffer

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform float type;
uniform float time;


uniform vec3 lightPosition;
uniform vec3 eyePosition;
uniform float colorType;
out vec2 texCoord;

out vec3 aPosition;
out vec3 aNormal;
out vec3 lightVec;
out vec3 eyeVec;

const float PI = 3.1415;


//sferic
vec3 getSphere(vec2 vec) {
    float az = vec.x * PI;// <-1;1> -> <-PI;PI>
    float ze = vec.y * PI / 2.0;// <-1;1> -> <-PI/2;PI/2>
    float r = 1.0;

    float x = r * cos(az) * cos(ze);
    float y =  r * sin(az) * cos(ze);
    float z =  r * sin(ze);
    return vec3(x, y, z);
}
vec3 getSphereNormal(vec2 vec) {
    vec3 u = getSphere(vec + vec2(0.001, 0)) - getSphere(vec - vec2(0.001, 0));
    vec3 v = getSphere(vec + vec2(0, 0.001)) - getSphere(vec - vec2(0, 0.001));

    return cross(u, v);
}


//sferic
vec3 getDonut(vec2 vec) {
    float R = 1;
    float r =  0.5;
    float u =  PI * 0.5 - PI * vec.x;
    float v =  PI * 0.5 - PI * vec.y / 2;

    float x =(R + r*cos(v))*cos(u);
    float y =(R + r*cos(v))*sin(u);
    float z = r*sin(v);
    return vec3(x, y, z);
}
vec3 getDonutNormal(vec2 vec) {
    vec3 u = getDonut(vec + vec2(0.001, 0)) - getDonut(vec - vec2(0.001, 0));
    vec3 v = getDonut(vec + vec2(0, 0.001)) - getDonut(vec - vec2(0, 0.001));

    return cross(u, v);
}


////sferic
//vec3 getSombrero(vec2 inPos) {
//    float s = PI * 0.5 - PI * inPos.x *2;
//    float t = 2 * PI * inPos.y;
//
//    float x =  t*cos(s);
//    float y =  t*sin(s);
//    float z = 2*sin(t)/2;
//
//    return vec3(x, y, z);
//}

//cylinder
vec3 getFunnel(vec2 vec) {
    float s = PI * 0.1 - PI * vec.x;
    float t = PI * 0.1 - PI * vec.y /2;

    float x =  t*cos(s);
    float y =  t*sin(s);
    float z = t;

    return vec3(x, y, z);
}

vec3 getFunnelNormal(vec2 vec) {
    vec3 u = getFunnel(vec + vec2(0.001, 0)) - getFunnel(vec - vec2(0.001, 0));
    vec3 v = getFunnel(vec + vec2(0, 0.001)) - getFunnel(vec - vec2(0, 0.001));

    return cross(u, v);
}


//cylinder
vec3 getCylinder(vec2 vec) {
    float s = PI * 0.1 - PI * vec.x;
    float t = PI * 0.1 - PI * vec.y/2;

    float x =  2*cos(s);
    float y =  2*sin(s);
    float z = t;

    return vec3(x, y, z);
}
vec3 getCylinderNormal(vec2 vec) {
    vec3 u = getCylinder(vec + vec2(0.001, 0)) - getCylinder(vec - vec2(0.001, 0));
    vec3 v = getCylinder(vec + vec2(0, 0.001)) - getCylinder(vec - vec2(0, 0.001));

    return cross(u, v);
}


//kartez
vec3 getPlot(vec2 vec) {
    return vec3(vec.x,vec.y,0.5 * cos(sqrt(20 * vec.x * vec.x + 20 * vec.y * vec.y)));
}
vec3 getPlotNormal(vec2 vec) {
    vec3 u = getPlot(vec + vec2(0.001, 0)) - getPlot(vec - vec2(0.001, 0));
    vec3 v = getPlot(vec + vec2(0, 0.001)) - getPlot(vec - vec2(0, 0.001));
    return cross(u, v);

}

//kartez
vec3 getArc(vec2 vec) {
    float position = cos(vec.x + time);
    return vec3(position, vec.y, 0.5 * cos(sqrt(vec.x * vec.x +  vec.y * vec.y)));
}
vec3 getArcNormal(vec2 vec) {
    vec3 u = getArc(vec + vec2(0.001, 0)) - getArc(vec - vec2(0.001, 0));
    vec3 v = getArc(vec + vec2(0, 0.001)) - getArc(vec - vec2(0, 0.001));
    return cross(u, v);

}



//kartez
vec3 getButterfly(vec2 vec) {
    //    return 0.5 * cos(sqrt(20 * vec.x * vec.x + 20 * vec.y * vec.y));
    return vec3(cos(vec.x),vec.y,vec.x*vec.y);
}
vec3 getButterflyNormal(vec2 vec) {
    vec3 u = getButterfly(vec + vec2(0.001, 0)) - getButterfly(vec - vec2(0.001, 0));
    vec3 v = getButterfly(vec + vec2(0, 0.001)) - getButterfly(vec - vec2(0, 0.001));

    return cross(u, v);
}


//kartez
vec3 getBananaPeel(vec2 vec) {

    float x =  vec.x * (vec.y * vec.y);
    float y =  vec.y * (vec.x * vec.x);
    float z = 1 - sqrt((vec.x*vec.x) + (vec.y * vec.y));

    return vec3(x, y, z);
}

vec3 getBananaPeelNormal(vec2 vec) {

    vec3 u = getBananaPeel(vec + vec2(0.001, 0)) - getBananaPeel(vec - vec2(0.001, 0));
    vec3 v = getBananaPeel(vec + vec2(0, 0.001)) - getBananaPeel(vec - vec2(0, 0.001));

    return cross(u, v);
}

vec3 getPlane(vec2 vec) {
    return vec3(vec * 2.5, -1);
}
vec3 getPlaneNormal(vec2 vec) {
    vec3 u = getPlane(vec + vec2(0.001, 0)) - getPlane(vec - vec2(0.001, 0));
    vec3 v = getPlane(vec + vec2(0, 0.001)) - getPlane(vec - vec2(0, 0.001));
    return cross(u, v);
}






void main() {
    texCoord = inPosition;
    // grid je <0;1> - chci <-1;1>
    vec2 position = inPosition * 2 - 1;


    vec3 finalPosition;
    vec3 normal;
    if (type == 0) {
        normal = getBananaPeelNormal(position);
        finalPosition = getBananaPeel(position);
    }
    if (type == 1) {
        normal = getPlotNormal(position);
        finalPosition = getPlot(position);
    }
    if (type == 2){
        normal = getButterflyNormal(position);
        finalPosition =  getButterfly(position);
    }
    if (type == 3){
        normal = getArcNormal(position);
        finalPosition =  getArc(position);

    }
    if (type == 4){
        normal = getDonutNormal(position);
        position.y = sin(position.y + time);
        finalPosition = getDonut(position);
    }
    if (type == 5){
        normal = getSphereNormal(position);
        finalPosition = getSphere(position);
    }
    if (type == 6){
        normal = getFunnelNormal(position);
        finalPosition = getFunnel(position);
    }
    if (type == 7){
        normal = getCylinderNormal(position);
        finalPosition = getCylinder(position);
    }
    //TODO: dodelat zobrazeni polohy svetla - udelat kouli a
//    finalPosition = getLightSphere();
    aPosition = finalPosition;
    aNormal = normal;
    lightVec = normalize(lightPosition - finalPosition);
    //TODO: Reflektorovy zdroj - lightVec je ld z prednasky shaders
    //TODO:spotCutoff bude kolem 1 napr 0.99 ale ne > 1
    eyeVec = normalize(eyePosition - finalPosition);

    //TODO: pridat dalsi kameru kvuli modifikaci polohy svetla, aby svetlo jezdilo sem a tam



    vec4 pos4 = vec4(finalPosition, 1.0);
    gl_Position = projection * view *  model * pos4;


} 
