#version 150
in vec2 inPosition;// input from the vertex buffer
uniform int  shake;
out vec2 texCoord;
uniform float height;
uniform float time;
void main() {

    texCoord = inPosition;

    //    float offset =  (1000.0 * 2*3.14159 * 0.75);
    //    texCoord.x += cos(texCoord.y + time) / 100;


    vec2 position = inPosition * 2 - 1;
    gl_Position = vec4(position, 0, 1.0);
    //TODO: shake
        if (shake == 1)
        {
            float strength = 0.01;
            gl_Position.x += cos(time * 10) * strength;
            gl_Position.y += cos(time * 15) * strength;
        }

//    TODO:Blur
//    float strength = 0.3;
//    vec2 pos = vec2(texture.x + sin(time) * strength, texture.y + cos(time) * strength);
//    TexCoords = pos;

    // grid je <0;1> - potřebujeme <-1;1>, protože takový je rozsah obrazovky

} 
