#version 150
in vec2 texCoord;

uniform sampler2D textureForObjects;

out vec4 outColor;// output from the fragment shader
in vec3 aPosition;
in vec3 aNormal;

uniform float colorType;
uniform vec3 lightPosition;
uniform vec3 eyePosition;
void main() {
    vec4 textureColor = texture(textureForObjects, texCoord);
    vec4 ambient = vec4(0.2,0.2,0.2,1);
    //TODO: dodelat lightposition posilat z rendereru nejakou solidni pozici
    float nDotL= max(dot(normalize(aNormal),normalize(lightPosition)),0);
    //TODO: dopracovat se k zrcadlovemu osvetleni pow(NdotH, 16)
    //TODO: tohle nejak blbne
    vec3 halfVec = normalize(normalize(eyePosition) + normalize(lightPosition));
    float nDotH = dot(aNormal, halfVec);
    nDotH = max(0,nDotH);
    nDotH = pow(nDotH, 16);

//    float nDotH = pow(max(0,dot(aNormal,halfVec)),16);

    vec4 diffuse = vec4(nDotL*vec3(0.6),1);
    vec4 spec = vec4(nDotH*vec3(0.6),1);
    //TODO: sem pricist zrcadlovou slozku
    vec4 finalColor = ambient + diffuse ;//+ spec;
    if (colorType == 0){
        outColor = textureColor;
    }

    if (colorType == 1){
        outColor = vec4(texCoord, 1.0, 1.0);
    }
    if (colorType == 2){
        outColor = vec4(aPosition, 1.0);
    }
    if (colorType == 3){
        outColor = finalColor * textureColor;
    }
    //TODO: dodelat normalu toto nefunguje je to cerny
    if (colorType == 4){
        outColor = vec4(aNormal, 1.0);
    }


    //	outColor = vec4(1.0, 0.0, 0.0, 1.0);


} 
