#version 150
in vec2 texCoord;

uniform sampler2D textureForObjects;

out vec4 outColor;// output from the fragment shader
in vec3 aPosition;
in vec3 aNormal;
in vec3 lightDirection;
in vec3 eyeVec;

uniform float colorType;
uniform vec3 lightPosition;
uniform vec3 eyePosition;
void main() {
    vec4 textureColor = texture(textureForObjects, texCoord);
    vec4 ambient = vec4(0.2, 0.2, 0.2, 1);
    float nDotL= max(dot(normalize(aNormal), normalize(lightPosition)), 0);
    vec3 ld = normalize(lightDirection);
    vec3 vd = normalize(eyeVec);
    vec3 nd = normalize(aNormal);

    vec3 halfVec = normalize(vd + ld);
    float nDotH = dot(nd, halfVec);
    nDotH = max(0, nDotH);
    nDotH = pow(nDotH, 16);

    vec4 diffuse = vec4(nDotL*vec3(1.5), 1);
    vec4 spec = vec4(nDotH*vec3(0.6), 1);
    vec4 finalColor = ambient  + spec + diffuse;

    if (colorType == 0) outColor = textureColor;
    if (colorType == 1) outColor = vec4(texCoord, 1.0, 1.0);
    if (colorType == 2) outColor = vec4(aPosition, 1.0);
    if (colorType == 3) outColor = finalColor;//* textureColor;
    if (colorType == 4) outColor = vec4(normalize(aNormal), 1.0);
    //light color
    if (colorType == 5) outColor = vec4(1);


    //	outColor = vec4(1.0, 0.0, 0.0, 1.0);


} 
