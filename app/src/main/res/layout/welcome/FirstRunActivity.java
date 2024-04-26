package layout.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.eluded.privacymanager.MainActivity;
import com.eluded.privacymanager.Preferences;
import com.eluded.privacymanager.R;

import java.util.UUID;

public class FirstRunActivity extends AppCompatActivity {

	private ViewPager viewPager;
	private MyViewPagerAdapter myViewPagerAdapter;
	private LinearLayout dotsLayout;
	private TextView[] dots;
	private int[] layouts;
	private Button btnSkip, btnNext;
	private String secretCode;
	private Preferences prefs; // Add this line

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		prefs = new Preferences(this, false); // Add this line

		if (!prefs.isFirstTimeLaunch()) {
			launchHomeScreen();
			finish();
		}

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.first_run_activity);
//
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
		btnSkip = (Button) findViewById(R.id.btn_skip);
		btnNext = (Button) findViewById(R.id.btn_next);
		btnSkip.setEnabled(false);
		btnSkip.setVisibility(View.INVISIBLE);

		// layouts of all welcome sliders
		// add few more layouts if you want
		layouts = new int[]{
				R.layout.welcome_slide_logo,
				R.layout.welcome_slide_disclaimer,
				R.layout.fragment_welcome_slide_panicwipe,
				R.layout.welcome_slide_networking,
				R.layout.welcome_slide_secretcode
		};

		// adding bottom dots
		addBottomDots(0);

		// making notification bar transparent
//		changeStatusBarColor();

		myViewPagerAdapter = new MyViewPagerAdapter();
		viewPager.setAdapter(myViewPagerAdapter);
		viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


		btnSkip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchHomeScreen();
			}
		});


		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// checking for last page
				// if last page home screen will be launched
				if (layouts[getItem(0)] ==  R.layout.fragment_welcome_slide_panicwipe) {

					CheckBox wipeusb = 	viewPager.findViewById(R.id.checkbox_wipeusb);
					CheckBox wipecode = viewPager.findViewById(R.id.checkbox_wipecode);
					Boolean shouldAddSecretCodeScreen = (wipecode.isChecked() || wipeusb.isChecked());
					Log.d("VIEW: ", shouldAddSecretCodeScreen.toString());

					if(shouldAddSecretCodeScreen) {
//						layouts = Arrays.copyOf(layouts, layouts.length + 1);
//						layouts[layouts.length - 1] = R.layout.welcome_slide_secretcode;
					}
				}

				if(layouts[getItem(0)] == R.layout.welcome_slide_secretcode) {
					EditText secretcodeedit = 	viewPager.findViewById(R.id.secretcode);
					secretCode = secretcodeedit.getText().toString();
				}

				int current = getItem(+1);

				if (current < layouts.length) {
					// move to next screen
					viewPager.setCurrentItem(current);
				} else {
					launchHomeScreen();
				}
			}
		});
	}

	private void addBottomDots(int currentPage) {
		dots = new TextView[layouts.length];

		int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
		int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

		dotsLayout.removeAllViews();
		for (int i = 0; i < dots.length; i++) {
			dots[i] = new TextView(this);
			dots[i].setText(Html.fromHtml("&#8226;"));
			dots[i].setTextSize(35);
			dots[i].setTextColor(colorsInactive[currentPage]);
			dotsLayout.addView(dots[i]);
		}

		if (dots.length > 0)
			dots[currentPage].setTextColor(colorsActive[currentPage]);
	}

	private int getItem(int i) {
		return viewPager.getCurrentItem() + i;
	}

	private void launchHomeScreen() {
		prefs.setFirstTimeLaunch(false);
//		prefs.setSecret(secretCode);
		startActivity(new Intent(FirstRunActivity.this, MainActivity.class));
		finish();
	}

	//  viewpager change listener
	ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			addBottomDots(position);

			// changing the next button text 'NEXT' / 'GOT IT'
			if (isLayout(position, layouts.length - 1)) {
				// last page. make button text to GOT IT
//				btnNext.setText(getString(R.string.start));
				btnSkip.setVisibility(View.GONE);
			} else {
				// still pages are left
//				btnNext.setText(getString(R.string.next));
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	/**
	 * Making notification bar transparent
	 */
	private void changeStatusBarColor() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
		}
	}

	/**
	 * View pager adapter
	 */
	public class MyViewPagerAdapter extends PagerAdapter {
		private LayoutInflater layoutInflater;

		public MyViewPagerAdapter() {
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = layoutInflater.inflate(layouts[position], container, false);
			// Check if the layout is R.layout.welcome_slide_secretcode
			if (isLayout(position, R.layout.welcome_slide_secretcode)) {
				// Set text to the secretcode EditText
				EditText secretCodeEditText = view.findViewById(R.id.secretcode); // replace R.id.secretcode with the actual ID
				secretCodeEditText.setText(UUID.randomUUID().toString());
			}

			if(isLayout(position, R.layout.welcome_slide_disclaimer)) {

				CheckBox disclamerAccepted = (CheckBox) view.findViewById(R.id.checkbox_disclaimer);
				disclamerAccepted.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
						btnSkip.setEnabled(b);
						btnSkip.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
					}
				});
			}

			if(isLayout(position, R.layout.welcome_slide_networking)) {
				Spinner spinner = view.findViewById(R.id.networking_mode);
				// Create an ArrayAdapter using the string array and a default spinner layout.
				ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
						view.getContext(),
						R.array.networking_modes,
						android.R.layout.simple_spinner_item
				);
// Specify the layout to use when the list of choices appears.
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
				spinner.setAdapter(adapter);
			}

			container.addView(view);

			return view;
		}

		@Override
		public int getCount() {
			return layouts.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}


		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}
	}

	private boolean isLayout(int position, int screenToCheck) {
		return layouts[position] == screenToCheck;

	}
}
