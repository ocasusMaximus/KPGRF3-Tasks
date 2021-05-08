#version 150
in vec2 inPosition;// input from the vertex buffer
uniform int  shake;
out vec2 texCoord;
uniform float height;
uniform float time;


void shakeEffect(){

    float strength = 0.01;

    gl_Position.x += cos(time * 10) * strength;
    gl_Position.y += cos(time * 15) * strength;

}
void main() {

    texCoord = inPosition;

    vec2 position = inPosition * 2 - 1;
    gl_Position = vec4(position, 0, 1.0);



} 
