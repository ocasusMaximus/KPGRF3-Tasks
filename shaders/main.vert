#version 150
in vec2 inPosition;// input from the vertex buffer

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform float type;
uniform float time;


uniform vec3 yellowLightPosition;
uniform vec3 redLightPosition;
uniform vec3 eyePosition;

uniform  float spotCutOff;

uniform float colorType;
out vec2 texCoord;

out vec3 objectPosition;
out vec3 normalDirection;
out vec3 yellowLightDirection;
out vec3 redLightDirection;
out vec3 eyeVec;
out float yellowLightDistance;
out float redLightDistance;



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

vec3 getLightSphere(vec2 vec) {
    float az = vec.x * PI;// <-1;1> -> <-PI;PI>
    float ze = vec.y * PI / 2.0;// <-1;1> -> <-PI/2;PI/2>
    float r = 0.1;

    float x = r * cos(az) * cos(ze);
    float y =  r * sin(az) * cos(ze);
    float z =  r * sin(ze);
    return vec3(x, y, z);
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



//cylinder
vec3 getFunnel(vec2 vec) {
    float s =  PI * vec.x;
    float t = PI * vec.y /2;

    float x =  t*cos(s)/2;
    float y =  t*sin(s)/2;
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
    float s = PI * vec.x;
    float t =   vec.y/2;

    float x =  cos(s)/2;
    float y =  sin(s)/2;
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
    return vec3(vec.x, vec.y, 0.5 * cos(sqrt(20 * vec.x * vec.x + 20 * vec.y * vec.y)));
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
    return vec3(cos(vec.x), vec.y, vec.x*vec.y);
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





void main() {
    texCoord = inPosition;

    vec2 position = inPosition * 2 - 1;


    vec3 finalPosition;
    vec3 normal;
    if (type == 0) {
        normal = getPlotNormal(position);
        finalPosition = getPlot(position);
    }
    if (type == 1) {
        normal = getBananaPeelNormal(position);
        finalPosition = getBananaPeel(position);

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
        position.y = position.y + time;
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
    if (type == 8){
        finalPosition = getLightSphere(position);
    }




    objectPosition = finalPosition;
    normalDirection = inverse(transpose(mat3(model))) * normal;

   vec4 finalPos4 = model * vec4(finalPosition,1.0);
    yellowLightDirection = normalize(yellowLightPosition - finalPos4.xyz);
    redLightDirection = normalize(redLightPosition - finalPos4.xyz);


    eyeVec = normalize(eyePosition - finalPos4.xyz);

    yellowLightDistance = length(yellowLightPosition - finalPos4.xyz);
    redLightDistance = length(redLightPosition - finalPos4.xyz);





    vec4 pos4 = vec4(finalPosition, 1.0);
    gl_Position = projection * view *  model * pos4;


} 
