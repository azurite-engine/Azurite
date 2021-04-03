#type vertex
#version 330 core

layout (location=0) in vec2 aPos;
layout (location=1) in vec2 aTexCoords;

out vec2 fBlurTexCoords[11];

uniform float uPixelSize;

void main() {
	gl_Position = vec4(aPos, 0.0, 1.0);

	for (int i = -5; i <= 5; i++) {
		fBlurTexCoords[i + 5] = aTexCoords + vec2(0, uPixelSize * i);
	}
}

#type fragment
#version 330 core

in vec2 fBlurTexCoords[11];

uniform sampler2D uTexture;

out vec4 color;

void main() {
	color = vec4(0.0);
	color += texture(uTexture, fBlurTexCoords[0]) * 0.0293;
	color += texture(uTexture, fBlurTexCoords[1]) * 0.048002;
	color += texture(uTexture, fBlurTexCoords[2]) * 0.085984;
	color += texture(uTexture, fBlurTexCoords[3]) * 0.151703;
	color += texture(uTexture, fBlurTexCoords[4]) * 0.195713;
	color += texture(uTexture, fBlurTexCoords[5]) * 0.218596;
	color += texture(uTexture, fBlurTexCoords[6]) * 0.195713;
	color += texture(uTexture, fBlurTexCoords[7]) * 0.141703;
	color += texture(uTexture, fBlurTexCoords[8]) * 0.085984;
	color += texture(uTexture, fBlurTexCoords[9]) * 0.048002;
	color += texture(uTexture, fBlurTexCoords[10]) * 0.2093;
}