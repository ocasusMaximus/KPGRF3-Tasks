#version 150
in vec2 texCoord;

uniform sampler2D textureForObjects;

uniform float constantAttenuation, linearAttenuation, quadraticAttenuation, spotCutOff;
out vec4 outColor;
in vec3 objectPosition;
in vec3 normalDirection;
in vec3 yellowLightDirection;
in vec3 redLightDirection;
in vec3 eyeVec;
in float yellowLightDistance;
in float redLightDistance;

uniform float colorType;
uniform vec3 yellowLightPosition;
uniform vec3 redLightPosition;
uniform vec3 eyePosition;
void main() {
    vec4 textureColor = texture(textureForObjects, texCoord);
    vec3 yld = normalize(yellowLightDirection);
    vec3 rld = normalize(redLightDirection);
    vec3 vd = normalize(eyeVec);
    vec3 nd = normalize(normalDirection);

    float constantAttenuation = 1.0;
    float linearAttenuation = 0.1;
    float quadraticAttenuation = 0.01;


    vec3 yellowSpotDirection = -yellowLightPosition;
    vec3 redSpotDirection = -redLightPosition;




    float yellowNDotL= max(dot(nd, yld), 0);
    float redNDotL= max(dot(nd, rld), 0);

    vec3 yellowHalfVec = normalize(vd + yld);
    float yellowNDotH = pow(max(0, dot(nd, yellowHalfVec)), 16);

    vec3 redHalfVec = normalize(vd + rld);
    float redNDotH = pow(max(0, dot(nd, redHalfVec)), 16);


    float yellowAttentuation=1.0/(constantAttenuation +
    linearAttenuation * yellowLightDistance +
    quadraticAttenuation * yellowLightDistance * yellowLightDistance);

    float redAttentuation=1.0/(constantAttenuation +
    linearAttenuation * redLightDistance +
    quadraticAttenuation * redLightDistance * redLightDistance);



    vec4 ambient = vec4(0.2, 0.2, 0.2, 1);

    vec4 yellowDiffuse = vec4(yellowNDotL*vec3(1.5), 1);
    vec4 yellowSpec = vec4(yellowNDotH*vec3(0.6), 1);

    vec4 redDiffuse = vec4(redNDotL*vec3(1.5), 1);
    vec4 redSpec = vec4(redNDotH*vec3(0.6), 1);

    yellowDiffuse = vec4(yellowDiffuse.rgb * vec3(0.8, 0.8, 0.255), 1.0f);
    yellowSpec = vec4(yellowSpec.rgb * vec3(0.8, 0.8, 0.255), 1.0f);

    redDiffuse = vec4(redDiffuse.rgb * vec3(0.8, 0.255, 0.255), 1.0f);
    redSpec = vec4(redDiffuse.rgb * vec3(0.8, 0.255, 0.255), 1.0f);

    float yellowSpotEffect = max(dot(normalize(yellowSpotDirection), normalize(-yld)), 0);
    float yellowBlend = clamp((yellowSpotEffect-spotCutOff)/(1-spotCutOff), 0.0, 1.0);//orezani na rozsah <0;1>

    float redSpotEffect = max(dot(normalize(redSpotDirection), normalize(-rld)), 0);
    float redBlend = clamp((redSpotEffect-spotCutOff)/(1-spotCutOff), 0.0, 1.0);//orezani na rozsah <0;1>
    vec4 lighting = ambient;





    if (colorType == 0) outColor = textureColor;
    if (colorType == 1) outColor = vec4(texCoord, 1.0, 1.0);
    if (colorType == 2) outColor = vec4(objectPosition, 1.0);
    if (colorType == 3) outColor = vec4(nd, 1.0);
    if (colorType == 4) {

        lighting = ambient  + yellowAttentuation * (yellowSpec + yellowDiffuse) + redAttentuation * (redSpec + redDiffuse);

        outColor = lighting;

    }
    if (colorType == 5) {

        lighting = ambient  + yellowAttentuation * (yellowSpec + yellowDiffuse) + redAttentuation * (redSpec + redDiffuse);

        outColor = lighting* textureColor;

    }
    if (colorType == 6){

        if (yellowSpotEffect > spotCutOff)  lighting = mix(ambient, ambient  + yellowAttentuation * (yellowSpec + yellowDiffuse), yellowBlend);
        if (redSpotEffect > spotCutOff)  lighting =  mix(ambient, ambient  + redAttentuation * (redSpec + redDiffuse), redBlend);
        outColor = lighting;
    }
    if (colorType == 7){

        if (yellowSpotEffect > spotCutOff)  lighting = mix(ambient, ambient  + yellowAttentuation * (yellowSpec + yellowDiffuse), yellowBlend);
        if (redSpotEffect > spotCutOff)  lighting =  mix(ambient, ambient  + redAttentuation * (redSpec + redDiffuse), redBlend);
        outColor = lighting* textureColor;
    }

    //light color
    if (colorType == 8) outColor = vec4(vec3(0.8, 0.8, 0.255), 1.0f);
    if (colorType == 9) outColor = vec4(vec3(0.8, 0.255, 0.255), 1.0f);


}
