#version 330 core

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in vec3 aNormal;

out vec2 TexCoord;
out vec3 FragPos; // Position to pass to the fragment shader
out vec3 Normal;  // Normal to pass to the fragment shader

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform vec3 viewPos; // Add this line

void main()
{
    FragPos = vec3(model * vec4(aPos, 1.0)); // Transform the vertex position
    Normal = mat3(transpose(inverse(model))) * aNormal;  // Correct for model transformation
    TexCoord = aTexCoord;
    gl_Position = projection * view * model * vec4(aPos, 1.0);
}
