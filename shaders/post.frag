#version 150
in vec2 texCoord;

uniform sampler2D textureRendered;
uniform float height;
uniform float width;
uniform float effectStrength;
uniform float postColorType;
out vec4 outColor;

//TODO: Dodelat typy effectu jeste nevim ani kolik jich bude
//TODO: zkusit nejaky zajimavy effect
//TODO: menit nejak silu toho effectu kdyz to pujde
//TODO Optional: nejaka cara ktera oddeluje co je effect a co ne, nejlepe kdyby s ni slo pohybovat a menit tak dynamicky plochu effectu


void main() {
    vec4 textureColor = texture(textureRendered, texCoord);


    if (gl_FragCoord.x < width/2) {

        if(postColorType == 0){
            float pixels = 2048.0;
            float pixel_w = 15.0;
            float pixel_h = 10.0;

            float dx = pixel_w*(1.0/pixels);
            float dy = pixel_h*(1.0/pixels);
            vec2 coord = vec2(dx*floor(texCoord.x/dx), dy*floor(texCoord.y/dy));


            outColor = texture(textureRendered, coord);

        }
        if(postColorType == 1){
            outColor = 1 -textureColor;
        }
        if(postColorType == 2){
            outColor = textureColor.rbga;
        }



    } else {
        outColor = textureColor;
    }


} 
