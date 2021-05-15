#version 150
in vec2 inPosition;// input from the vertex buffer
out vec2 texCoord;
uniform float time;

void main() {

    texCoord = inPosition;

    vec2 position = inPosition * 2 - 1;
    vec2 finalPosition = position;

    gl_Position = vec4(finalPosition, 0, 1.0);


}
