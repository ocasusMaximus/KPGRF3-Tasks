#version 150
in vec2 texCoord;

uniform sampler2D textureForObjects;

uniform float constantAttenuation, linearAttenuation, quadraticAttenuation;




out vec4 outColor;// output from the fragment shader
in vec3 objectPosition;
in vec3 normalDirection;
in vec3 lightDirection;
in vec3 eyeVec;
in float distance;

uniform float colorType;
uniform vec3 lightPosition;
uniform vec3 eyePosition;
void main() {
    vec4 textureColor = texture(textureForObjects, texCoord);
    vec3 ld = normalize(lightDirection);
    vec3 vd = normalize(eyeVec);
    vec3 nd = normalize(normalDirection);

    float constantAttenuation = 1.0;
    float linearAttenuation = 0.1;
    float quadraticAttenuation = 0.01;
//TODO: tlacitkem menit jeste spot cutoff mezi 0.9 - 1
    float spotCutOff = 0.98;
    vec3 spotDirection = -lightPosition;




    float nDotL= max(dot(nd, ld), 0);

    vec3 halfVec = normalize(vd + ld);
    float nDotH = pow(max(0, dot(nd, halfVec)), 16);


    float attentuation=1.0/(constantAttenuation +
    linearAttenuation * distance +
    quadraticAttenuation * distance * distance);



    vec4 ambient = vec4(0.2, 0.2, 0.2, 1);
    vec4 diffuse = vec4(nDotL*vec3(1.5), 1);
    vec4 spec = vec4(nDotH*vec3(0.6), 1);

    diffuse = vec4(diffuse.rgb * vec3(0.8, 0.8, 0.255), 1.0f);
    spec = vec4(spec.rgb * vec3(0.8, 0.8, 0.255), 1.0f);

    float spotEffect = max(dot(normalize(spotDirection), normalize(-ld)), 0);

    float blend = clamp((spotEffect-spotCutOff)/(1-spotCutOff), 0.0, 1.0);//orezani na rozsah <0;1>

    vec4 lighting = ambient;





    if (colorType == 0) outColor = textureColor;
    if (colorType == 1) outColor = vec4(texCoord, 1.0, 1.0);
    if (colorType == 2) outColor = vec4(objectPosition, 1.0);
    if (colorType == 3) outColor = vec4(nd, 1.0);
    if (colorType == 4) {

        outColor = lighting;//* textureColor;
    }
    if (colorType == 5) {

        lighting = ambient  + attentuation * (spec + diffuse);
        //                lighting.rgb = vec3(0.8,0.8,0.255);
        outColor = lighting;//* textureColor;
        //        outColor = vec4(lighting.rgb * vec3(0.8, 0.8, 0.255), 1.0f)* textureColor;
    }
    if (colorType == 6){

        if (spotEffect > spotCutOff)  lighting = mix(ambient, ambient  + attentuation * (spec + diffuse), blend);
        outColor = lighting; //* textureColor;
    }

    //light color
    if (colorType == 7) outColor = vec4(vec3(0.8, 0.8, 0.255), 1.0f);


}
