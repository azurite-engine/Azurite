#type vertex
#version 330 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec2 fPos;
out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;

void main() {
    vec4 pos = uProjection * uView * vec4(aPos, 1.0);
    fPos = pos.xy;
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;

    gl_Position = pos;
}

#type fragment
#version 330 core

in vec2 fPos;
in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];
uniform sampler2D uLightmap;

out vec4 color;

void main () {
    vec4 texColor;

    // This may look bad, but it is intentional, openGL minimum spec does not require dynamic indexing with variables into texture arrays, so this switch is required on AMD GPUs, and apparently on apple which DOESN'T SUPPORT CASTING!?!?!?!? (Hence the if/else rather than a switch).
    if (fTexId == 0) {
        texColor = fColor;
    } else if (fTexId == 1) {
        texColor = fColor * texture(uTextures[1], fTexCoords);
    } else if (fTexId == 2) {
        texColor = fColor * texture(uTextures[2], fTexCoords);
    } else if (fTexId == 3) {
        texColor = fColor * texture(uTextures[3], fTexCoords);
    } else if (fTexId == 4) {
        texColor = fColor * texture(uTextures[4], fTexCoords);
    } else if (fTexId == 5) {
        texColor = fColor * texture(uTextures[5], fTexCoords);
    } else if (fTexId == 6) {
        texColor = fColor * texture(uTextures[6], fTexCoords);
    } else if (fTexId == 7) {
        texColor = fColor * texture(uTextures[7], fTexCoords);
    }

    // Sample from lightmap and multiply with current fragment color
    texColor *= texture(uLightmap, (fPos + 1)/2);
    color = texColor;
}