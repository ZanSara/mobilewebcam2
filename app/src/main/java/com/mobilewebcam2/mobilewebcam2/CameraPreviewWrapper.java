/* Copyright 2012 Michael Haar

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.mobilewebcam2.mobilewebcam2;

import java.util.List;

import android.annotation.TargetApi;
import android.hardware.Camera;

@TargetApi(5)
public class CameraPreviewWrapper
{
	/* calling here forces class initialization */
	public static void checkAvailable() {}

	public static List<Camera.Size> getSupportedPreviewSizes(Camera.Parameters parameters)
	{
		return parameters.getSupportedPreviewSizes();
	}	
}