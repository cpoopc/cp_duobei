package com.cp.duobei.fragment.home;
import com.cp.duobei.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class ImgFragment extends Fragment {

	private int imageresid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View inflate = inflater.inflate(R.layout.fragment_img, null);
		ImageView imageView = (ImageView) inflate.findViewById(R.id.imageView1);
		imageView.setImageResource(imageresid);
		return inflate;
	}

	public void changeImg(int imageresid) {
		this.imageresid = imageresid;
	}

}
