#version 150
in vec2 texCoord;

uniform sampler2D textureRendered;
uniform float height;
out vec4 outColor;


void main() {

    vec4 textureColor = texture(textureRendered, texCoord);

    if (gl_FragCoord.y < height/3) {

        outColor = textureColor.rbga;
    } else {
        outColor = textureColor;
    }
}