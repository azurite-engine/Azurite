#type vertex
#version 330 core

layout (location=0) in vec2 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;
layout (location=4) in float sticky;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;

void main() {
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;

    if (sticky > 0.5) {
        // if 1 then stick to camera
        gl_Position = uProjection * vec4(aPos, 0.0, 1.0);
    } else {
        // if 0, then be affected by camera view matrix (move with world)
        gl_Position = uProjection * uView * vec4(aPos, 0.0, 1.0);
    }
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

// Font textures
uniform sampler2D uTextures[8];

out vec4 color;

void main () {
    vec4 texColor;
    
    if (fTexId == 0) {
        texColor = fColor * texture(uTextures[0], fTexCoords);
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

    color = texColor;
}