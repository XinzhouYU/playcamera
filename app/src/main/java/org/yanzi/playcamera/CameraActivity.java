package org.yanzi.playcamera;

import org.yanzi.playcamera.CameraInterface;
import org.yanzi.playcamera.CameraInterface.CamOpenOverCallback;
import org.yanzi.playcamera.preview.CameraGLSurfaceView;
import org.yanzi.playcamera.R;
import org.yanzi.playcamera.util.DisplayUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;

public class CameraActivity extends Activity {
	private static final String TAG = "yanzi";
	CameraGLSurfaceView glSurfaceView = null;
	ImageButton shutterBtn;
	float previewRate = -1f;
	private static Context sInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		setInstance(this);

		initUI();
		initViewParams();

		shutterBtn.setOnClickListener(new BtnListeners());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_camera, menu);
		return true;
	}

	private void initUI(){
		glSurfaceView = (CameraGLSurfaceView)findViewById(R.id.camera_textureview);
		shutterBtn = (ImageButton)findViewById(R.id.btn_shutter);
	}
	private void initViewParams(){
		LayoutParams params = glSurfaceView.getLayoutParams();
		Point p = DisplayUtil.getScreenMetrics(this);
		params.width = p.x;
		params.height = p.y;
		previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
		glSurfaceView.setLayoutParams(params);

		//手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
		LayoutParams p2 = shutterBtn.getLayoutParams();
		p2.width = DisplayUtil.dip2px(this, 80);
		p2.height = DisplayUtil.dip2px(this, 80);
		shutterBtn.setLayoutParams(p2);	

	}

	private class BtnListeners implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_shutter:
				CameraInterface.getInstance().doTakePicture();
				break;
			default:break;
			}
		}

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		glSurfaceView.bringToFront();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		glSurfaceView.onPause();
	}

	private void setInstance(Context context) {
		sInstance = context;
	}

	/*	要理解这里声明为static的函义，就是为了让其它类能够通过CameraActivity这个类获得Context，
	 *  而不需要实例化。
	 *  另外，Context还可以在实例化其它对象的时候传入。比如new DirectDrawer(Context context)。
	 */
	public static Context getInstance() {
		return sInstance;
	}
}
