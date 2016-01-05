package org.yanzi.playcamera.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;


//加载顶点Shader与片元Shader的工具类
public class ShaderUtil
{
    private static String TAG = "ShaderUtil";

    //加载制定shader的方法
    public static int loadShader
    (
            int shaderType, //shader的类型  GLES20.GL_VERTEX_SHADER(顶点)   GLES20.GL_FRAGMENT_SHADER(片元)
            String source   //shader的脚本字符串
    )
    {
        //创建一个新shader
        int shader = GLES20.glCreateShader(shaderType);
        //若创建成功则加载shader
        if (shader != 0)
        {
            //加载shader的源代码
            GLES20.glShaderSource(shader, source);
            //编译shader
            GLES20.glCompileShader(shader);
            //存放编译成功shader数量的数组
            int[] compiled = new int[1];
            //获取Shader的编译情况
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0)
            {//若编译失败则显示错误日志并删除此shader
                Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":");
                Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    //创建shader程序的方法
    public static int createProgram(String vertexSource, String fragmentSource)
    {
        //加载顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0)
        {
            return 0;
        }

        //加载片元着色器
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0)
        {
            return 0;
        }

        //创建程序
        int program = GLES20.glCreateProgram();
        //若程序创建成功则向程序中加入顶点着色器与片元着色器
        if (program != 0)
        {
            //向程序中加入顶点着色器
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            //向程序中加入片元着色器
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            //链接程序
            GLES20.glLinkProgram(program);
            //存放链接成功program数量的数组
            int[] linkStatus = new int[1];
            //获取program的链接情况
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            //若链接失败则报错并删除程序
            if (linkStatus[0] != GLES20.GL_TRUE)
            {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    //检查每一步操作是否有错误的方法
    public static void checkGlError(String op)
    {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        {
            Log.e("ES20_ERROR", op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    /* 从sh脚本中加载shader内容的方法
     * assets文件有最大限制：UNCOMPRESS_DATA_MAX
     * Resources需要从Activity.getResources()获得。
     * 这里从View.getResources()获得，可能是因为创建View时传入了Context。
     * 资源文件的获取可以参考：http://blog.sina.com.cn/s/blog_602f8770010142h9.html
     */
    public static String loadFromAssetsFile(String fname,Resources r)
    {
        String result=null;
        try
        {
            InputStream in=r.getAssets().open(fname);
            int ch=0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while((ch=in.read())!=-1)
            {
                baos.write(ch);
            }
            byte[] buff=baos.toByteArray();
            baos.close();
            in.close();
            result=new String(buff,"UTF-8");
            result=result.replaceAll("\\r\\n","\n");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /* getRecources()是Contex的方法，所以必须是在Contex或其子类中调用。
     * Context的讲解可以参考http://www.cnblogs.com/wenjiang/archive/2012/10/15/2724923.html
     */
    public static String getShaderSource(Context context, int shaderSrc) {
        StringBuilder mStringBuilder = new StringBuilder();
        InputStreamReader isReader = new InputStreamReader(
                context.getResources().openRawResource(shaderSrc));
        BufferedReader mBufferedReader = new BufferedReader(isReader);

        try {
            for (String str = mBufferedReader.readLine(); str != null; str = mBufferedReader.readLine()) {
                mStringBuilder.append(str).append("\n");
            }
        } catch (IOException e) {
            Log.e(TAG, "read shader failed", e);
        }
        mStringBuilder.deleteCharAt(mStringBuilder.length() - 1);
        return mStringBuilder.toString();
    }
}