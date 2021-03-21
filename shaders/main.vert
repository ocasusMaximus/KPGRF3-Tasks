#version 150
in vec2 inPosition;// input from the vertex buffer

uniform mat4 view;
uniform mat4 projection;
uniform float type;

out vec2 texCoord;

const float PI = 3.1415;

float getZ(vec2 vec) {
    return 0.5 * cos(sqrt(20 * vec.x * vec.x + 20 * vec.y * vec.y));
}

vec3 getSphere(vec2 vec) {
    float az = vec.x * PI;// <-1;1> -> <-PI;PI>
    float ze = vec.y * PI / 2.0;// <-1;1> -> <-PI/2;PI/2>
    float r = 1.0;

    float x = r * cos(az) * cos(ze);
    float y = 2 * r * sin(az) * cos(ze);
    float z = 0.5 * r * sin(ze);
    return vec3(x, y, z);
}

//TODO: doresit donut
vec3 getDonut(vec2 vec) {
    float R = 2;
    float r =  1;
    float u =  PI * 0.5 - PI * vec.x ;
    float v =  PI * 0.5 - PI * vec.y / 2 ;

    float x =(R + r*cos(v))*cos(u);
    float y =(R + r*cos(v))*sin(u);
    float z = r*sin(v);
    return vec3(x, y, z);
}



vec3 getSombrero(vec2 inPos) {
    // sombrero
    float s = PI * 0.5 - PI * inPos.x *2;
    float t = 2 * PI * inPos.y;

    float x =  t*cos(s);
    float y =  t*sin(s);
    float z = 2*sin(t)/2;

    return vec3(x, y, z);
}



void main() {
    texCoord = inPosition;
    // grid je <0;1> - chci <-1;1>
    vec2 position = inPosition * 2 - 1;

    vec3 finalPosition;
    if (type == 0) {
        finalPosition = getSphere(position);
    	} else if(type == 1) {
    		finalPosition = vec3(position, getZ(position));
    	} else if( type == 2){
        finalPosition = getDonut(position);
    }
    vec4 pos4 = vec4(finalPosition, 1.0);
    gl_Position = projection * view * pos4;
} 
