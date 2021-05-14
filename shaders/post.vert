#version 150
in vec2 inPosition;// input from the vertex buffer
uniform int  shake;
out vec2 texCoord;
uniform float height;
uniform float time;
uniform float postType;
uniform float redLine;


void shakeEffect(){

    float strength = 0.01;

    gl_Position.x += cos(time * 10) * strength;
    gl_Position.y += cos(time * 15) * strength;

}

vec2 drawLine(){
    float x1= (100 -10.0f);
    float x2 = 100;
    float x = 10f;
    float y = 0.0f + height;
    return vec2(x, y);
}

void main() {

    texCoord = inPosition;

    vec2 position = inPosition * 2 - 1;
    vec2 finalPosition = position;
    if (postType == 1){
        finalPosition= vec2(100f,100f);
    }
    gl_Position = vec4(finalPosition, 0, 1.0);


}
