#type vertex
#version 330 core

layout (location=0) in vec2 aPos;
layout (location=1) in vec2 aTexCoords;

out vec2 fTexCoords;

void main() {
	fTexCoords = aTexCoords;

	gl_Position = vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 330 core

in vec2 fTexCoords;

uniform sampler2D uTexture;

out vec4 color;

void main() {
	vec4 sampled_color = texture(uTexture, fTexCoords);
	float brightness = (sampled_color.r * 0.2126) + (sampled_color.g * 0.7152) + (sampled_color.b * 0.0722);
	color = sampled_color * brightness*2.5;
}