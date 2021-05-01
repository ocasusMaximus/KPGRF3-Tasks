#version 150
in vec2 texCoord;

uniform sampler2D textureRendered;
uniform float height;
out vec4 outColor;// output from the fragment shader


void main() {

//    vec3 sample[9];
//if(chaos || shake){
//    for (int i = 0; i < 9; i++) sample[i] = vec3(texture(scene, TexCoords.st + offsets[i]));
//}
//    if (shake){
//        for (int i = 0; i < 9; i++){
//            outColor += vec4(blur_kernel[i], 0.0f);
//        }
//        outColor.a = 1.0f;
//    }




    vec4 textureColor = texture(textureRendered, texCoord);




    if (gl_FragCoord.y < height/3) {
        //		float grey = textureColor.r * 0.33 + textureColor.g * 0.33 + textureColor.b * 0.33;
        float lightGrey = textureColor.r * 0.7 + textureColor.g * 0.7 + textureColor.b * 0.7;

        //		outColor = vec4(lightGrey,lightGrey,lightGrey,1);
        outColor = 1- textureColor;
        outColor = textureColor.rbga;
    } else {
        outColor = textureColor;
    }
} 
