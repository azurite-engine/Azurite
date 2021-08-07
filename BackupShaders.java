package graphics.shaders;

import java.util.HashMap;

public class BackupShaders {

    /**
     * A map containing backups for shaders, in case they fail to compile
     */
    private static final HashMap<ShaderType, String> BACKUPS = new HashMap<>();

    static {

        // Simplest vertex shader
        BACKUPS.put(ShaderType.VERTEX,

                "#version 330 core \n" +
                "layout (location = 0) in vec3 aPos; \n" +
                "void main() { \n" +
                "    gl_Position = vec4(aPos, 1.0f); \n" +
                "}"

        );

        // Outputs a pink shader
        BACKUPS.put(ShaderType.FRAGMENT,

                "#version 330 core \n" +
                "out vec4 fragColor; \n" +
                "void main() { \n" +
                "    fragColor = vec4(1.0f, 0.0f, 1.0f, 1.0f); \n" +
                "}"

        );

        BACKUPS.put(ShaderType.GEOMETRY,

                "#version 330 core \n" +
                "void main() { \n" +
                "}"

        );

        BACKUPS.put(ShaderType.TESS_CONTROL,

                "#version 400 core \n" +
                "void main() { \n" +
                "}"

        );

        BACKUPS.put(ShaderType.TESS_EVALUATION,

                "#version 400 core \n" +
                "void main() { \n" +
                "}"

        );

        BACKUPS.put(ShaderType.COMPUTE,

                "#version 430 core \n" +
                "void main() { \n" +
                "}"

        );
    }

    /**
     * @param type The type of the shader that failed to compile
     * @return the backup shader associated with it
     */
    public static String get(ShaderType type) {
        return BACKUPS.get(type);
    }

}