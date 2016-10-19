package com.tutorialwing.androidfacebooksharevideotutorial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import static android.app.Activity.RESULT_OK;

public class ShareVideoFragment extends Fragment {

	private static final String TAG = ShareVideoFragment.class.getName();

	private CallbackManager callbackManager;

	ShareDialog shareDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize facebook SDK.
		FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

		// Create a callbackManager to handle the login responses.
		callbackManager = CallbackManager.Factory.create();

		shareDialog = new ShareDialog(getActivity());

		// this part is optional
		shareDialog.registerCallback(callbackManager, callback);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_share, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setShareButton(view);
	}

	private void setShareButton(View view) {
		Button shareButton = (Button) view.findViewById(R.id.shareButton);
		shareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("video/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK)
			return;

		if (requestCode == 1) {
			Uri videoFileUri = data.getData();
			ShareVideo video = new ShareVideo.Builder()
					.setLocalUrl(videoFileUri)
					.build();
			ShareVideoContent content = new ShareVideoContent.Builder()
					.setVideo(video)
					.build();

			shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
		} else {
			// Call callbackManager.onActivityResult to pass login result to the LoginManager via callbackManager.
			callbackManager.onActivityResult(requestCode, resultCode, data);
		}
	}


	private FacebookCallback<Sharer.Result> callback = new FacebookCallback<Sharer.Result>() {
		@Override
		public void onSuccess(Sharer.Result result) {
			Log.e(TAG, "Succesfully posted");
			// Write some code to do some operations when you shared content successfully.
		}

		@Override
		public void onCancel() {
			Log.e(TAG, "Cancel occured");
			// Write some code to do some operations when you cancel sharing content.
		}

		@Override
		public void onError(FacebookException error) {
			Log.e(TAG, error.getMessage());
			// Write some code to do some operations when some error occurs while sharing content.
		}
	};
}