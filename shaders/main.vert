#version 150
in vec2 inPosition;// input from the vertex buffer

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform float type;
uniform float time;


uniform float colorType;
out vec2 texCoord;

out vec3 aPosition;

const float PI = 3.1415;


//sferic
vec3 getSphere(vec2 vec) {
    float az = vec.x * PI;// <-1;1> -> <-PI;PI>
    float ze = vec.y * PI / 2.0;// <-1;1> -> <-PI/2;PI/2>
    float r = 1.0;

    float x = r * cos(az) * cos(ze);
    float y = 2 * r * sin(az) * cos(ze);
    float z = 0.5 * r * sin(ze);
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


//sferic
vec3 getSombrero(vec2 inPos) {
    float s = PI * 0.5 - PI * inPos.x *2;
    float t = 2 * PI * inPos.y;

    float x =  t*cos(s);
    float y =  t*sin(s);
    float z = 2*sin(t)/2;

    return vec3(x, y, z);
}

//cylinder
vec3 getFunnel(vec2 vec) {
    float s = PI * 0.1 - PI * vec.x;
    float t = PI * 0.1 - PI * vec.y /2;

    float x =  t*cos(s);
    float y =  t*sin(s);
    float z = t;

    return vec3(x, y, z);
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
//kartez
float getPlot(vec2 vec) {
    return 0.5 * cos(sqrt(20 * vec.x * vec.x + 20 * vec.y * vec.y));
}
//kartez
float getArc(vec2 vec) {
    return 0.5 * cos(sqrt(vec.x * vec.x +  vec.y * vec.y));
}


//kartez
float getHyperbolic(vec2 vec) {
    //    return 0.5 * cos(sqrt(20 * vec.x * vec.x + 20 * vec.y * vec.y));
    return vec.x*vec.y;
}


//kartez
vec3 getBananaPeel(vec2 vec) {

    float x =  vec.x * (vec.y * vec.y);
    float y =  vec.y * (vec.x * vec.x);
    float z = 1 - sqrt((vec.x*vec.x) + (vec.y * vec.y));

    return vec3(x, y, z);
}







void main() {
    texCoord = inPosition;
    // grid je <0;1> - chci <-1;1>
    vec2 position = inPosition * 2 - 1;


    vec3 finalPosition;
    //TODO: zkusit se podivat jestli se tohle neda nejak porefaktorovat aby nesel mimo ty cisla
    if (type == 0) {
        finalPosition = getBananaPeel(position);
    }
    if (type == 1) {
        finalPosition = vec3(position, getPlot(position));
    }
    if (type == 2){
        finalPosition = vec3(position, getHyperbolic(position));
    }
    if (type == 3){
        position.x = cos(position.x + time);
        finalPosition = vec3(position, getArc(position));
    }
    if (type == 4){
        position.y = cos(position.y + time);
        finalPosition = getDonut(position);
    }
    if (type == 5){
        finalPosition = getSphere(position);
    }
    if (type == 6){
        finalPosition = getFunnel(position);
    }
    if (type == 7){
        finalPosition = getCylinder(position);
    }
    aPosition = finalPosition;
    vec4 pos4 = vec4(finalPosition, 1.0);
    gl_Position = projection * view *  model * pos4;

} 
