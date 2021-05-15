#version 150
in vec2 texCoord;

uniform sampler2D textureRendered;
uniform float height;
uniform float width;
uniform float postColorType;
uniform float redLine;
out vec4 outColor;



void main() {
    vec4 textureColor = texture(textureRendered, texCoord);



    if (gl_FragCoord.x < redLine) {
        /* b=g change*/
        if(postColorType == 0){
            outColor = textureColor.rbga;
        }
        /*negativ*/
        if(postColorType == 1){
            outColor = 1 -textureColor;
        }
        /* pixelization */
        if(postColorType == 2){
            float pixels = 2048.0;
            float pixel_w = 15.0;
            float pixel_h = 10.0;

            float dx = pixel_w*(1.0/pixels);
            float dy = pixel_h*(1.0/pixels);
            vec2 coord = vec2(dx*floor(texCoord.x/dx), dy*floor(texCoord.y/dy));

            outColor = texture(textureRendered, coord);

        }
        /* gamma change */
        if(postColorType ==3){
            float gamma = 0.6;
            float numColors = 8.0;
            vec3 c = texture(textureRendered, texCoord).rgb;
            c = pow(c, vec3(gamma, gamma, gamma));
            c = c * numColors;
            c = floor(c);
            c = c / numColors;
            c = pow(c, vec3(1.0/gamma));
            outColor = vec4(c, 1.0);
        }
        /*dream vision */
        if(postColorType ==4){
            vec2 uv = texCoord.xy;
            vec4 c = texture(textureRendered, uv);

            c += texture(textureRendered, uv+0.001);
            c += texture(textureRendered, uv+0.003);
            c += texture(textureRendered, uv+0.005);
            c += texture(textureRendered, uv+0.007);
            c += texture(textureRendered, uv+0.009);
            c += texture(textureRendered, uv+0.011);

            c += texture(textureRendered, uv-0.001);
            c += texture(textureRendered, uv-0.003);
            c += texture(textureRendered, uv-0.005);
            c += texture(textureRendered, uv-0.007);
            c += texture(textureRendered, uv-0.009);
            c += texture(textureRendered, uv-0.011);

            c.rgb = vec3((c.r+c.g+c.b)/3.0);
            c = c / 9.5;
            outColor = c;
        }

        if(gl_FragCoord.x > redLine -3f && gl_FragCoord.x < redLine) outColor = vec4(vec3(0.8, 0.255, 0.255), 1.0f);


    } else {

            outColor = textureColor;

    }

}
