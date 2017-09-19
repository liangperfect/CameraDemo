package com.ckt.admin.myapplication.manager;

import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by admin on 2017/9/3.
 * current open single camera
 */

public class CameraManagerImp implements CameraManager {
    private static final int CAMERA_HAL_API_VERSION_1_0 = 0x100;

    private static final String TAG = "CameraManagerImpl";

    private CameraPorxy mCameraPorxy;

    public CameraManagerImp() {
        mCameraPorxy = new CameraProxyImpl();
    }

    @Override
    public int getCameraNums() {
        int nums = Camera.getNumberOfCameras();
        if (nums == 0)
            Log.d(TAG, "getCameraNums():camera num is 0 can not exe camera ");
        return nums;
    }

    @Override
    public CameraPorxy getCamera(int camId) {
        return mCameraPorxy.openCamera(camId);
    }

    @Override
    public Camera.Parameters getCameraParameters() {
        return mCameraPorxy.getCameraParameters();
    }

    @Override
    public void setCameraParameters(Camera.Parameters cameraParameters) {
        mCameraPorxy.setCameraParameters(cameraParameters);
    }

    @Override
    public void release() {
        mCameraPorxy.release();
    }


    /**
     * camera proxy impl
     */
    public class CameraProxyImpl implements CameraPorxy {

        Camera camera;

        public CameraProxyImpl() {

        }

        @Override
        public CameraProxyImpl openCamera(int camId) {
            /*可能有些系统有限制就要只能在运行时候用反射进行调用*/
            try {
                Method openMethod = Class.forName("android.hardware.Camera").getMethod(
                        "openLegacy", int.class, int.class);
                camera = (Camera) openMethod.invoke(null, camId, CAMERA_HAL_API_VERSION_1_0);
            } catch (Exception e) {
                camera = Camera.open(camId);
            }
            if (camera == null)
                return null;
            return this;
        }

        @Override
        public Camera.Parameters getCameraParameters() {
            return camera.getParameters();
        }

        @Override
        public void setCameraParameters(Camera.Parameters cameraParameters) {
            camera.setParameters(cameraParameters);
        }

        @Override
        public void setSurfaceHolder(SurfaceHolder surfaceHolder) {

            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void startPreview() {
            camera.startPreview();
        }

        @Override
        public void stopPreview() {
            camera.stopPreview();
        }

        @Override
        public void release() {
            camera.release();
            camera = null;
        }

        @Override
        public void setRotation() {

        }

        @Override
        public void setDisplayOrientation(int rotation) {
            camera.setDisplayOrientation(rotation);
        }
    }
}
