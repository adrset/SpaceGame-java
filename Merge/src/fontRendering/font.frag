#version 130

in vec2 pass_textureCoords;

out vec4 out_color;

uniform vec3 textColor;
uniform sampler2D fontAtlas;

void main(void){

	out_color = vec4(textColor, texture(fontAtlas, pass_textureCoords).a);
}