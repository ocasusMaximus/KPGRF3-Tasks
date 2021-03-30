#version 150
in vec2 texCoord;

uniform sampler2D textureForObjects;

out vec4 outColor;// output from the fragment shader
in vec3 aPosition;
uniform float colorType;
void main() {
    vec4 textureColor = texture(textureForObjects, texCoord);

//TODO: zkusit porefaktorovat aby nemohl vyjit z tehlech hodnot
    if (colorType == 0){
        outColor = textureColor;
    }

    if (colorType == 1){
        outColor = vec4(texCoord, 1.0, 1.0);
    }
    if (colorType == 2){
        outColor = vec4(aPosition, 1.0);
    }

    //	outColor = vec4(1.0, 0.0, 0.0, 1.0);


} 
