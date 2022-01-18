#type vertex
#version 330 core

layout (location=0) in vec2 aPos;
layout (location=1) in vec2 aTexCoords;

out vec2 fBlurTexCoords[11];

uniform float uPixelSize;

void main() {
	gl_Position = vec4(aPos, 0.0, 1.0);

	for (int i = -5; i <= 5; i++) {
		fBlurTexCoords[i + 5] = aTexCoords + vec2(uPixelSize * i, 0);
	}
}

#type fragment
#version 330 core

in vec2 fBlurTexCoords[11];

uniform sampler2D uTexture;

out vec4 color;

void main() {
	color = vec4(0.0);
	color += texture(uTexture, fBlurTexCoords[0])  * 0.0093;
	color += texture(uTexture, fBlurTexCoords[1])  * 0.028002;
	color += texture(uTexture, fBlurTexCoords[2])  * 0.065984;
	color += texture(uTexture, fBlurTexCoords[3])  * 0.131703;
	color += texture(uTexture, fBlurTexCoords[4])  * 0.175713;
	color += texture(uTexture, fBlurTexCoords[5])  * 0.198596;
	color += texture(uTexture, fBlurTexCoords[6])  * 0.175713;
	color += texture(uTexture, fBlurTexCoords[7])  * 0.121703;
	color += texture(uTexture, fBlurTexCoords[8])  * 0.065984;
	color += texture(uTexture, fBlurTexCoords[9])  * 0.028002;
	color += texture(uTexture, fBlurTexCoords[10]) * 0.0093;
}