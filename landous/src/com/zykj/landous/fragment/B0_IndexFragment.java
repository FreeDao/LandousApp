package com.zykj.landous.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.zykj.landous.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class B0_IndexFragment extends Fragment {
	private ImageView[] imageViews;
	private List<View> pageViews;
	private ImageView imageView;
	private AdPageAdapter adapter;
	ImageView[] img;
	private LinearLayout pagerLayout;
	private ViewPager adViewPager;
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	private boolean isContinue = true;
	/*
	 * ÿ���̶�ʱ���л������ͼƬ
	 */
	private final Handler viewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			adViewPager.setCurrentItem(msg.what);
			super.handleMessage(msg);

		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_index, null);
		init(view);
		return view;
	}

	private void init(View view) {
		// �Ӳ����ļ��л�ȡViewPager������
		pagerLayout = (LinearLayout) view.findViewById(R.id.view_pager_content);
		// ����ViewPager
		adViewPager = new ViewPager(getActivity());

		// ��ȡ��Ļ���������Ϣ
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

		// ������Ļ��Ϣ����ViewPager��������Ŀ��
		adViewPager.setLayoutParams(new LayoutParams(dm.widthPixels,
				dm.heightPixels * 2 /9));

		// ��ViewPager�������õ������ļ���������
		pagerLayout.addView(adViewPager);
		initPageAdapter();
		initCirclePoint(view);
		adViewPager.setAdapter(adapter);
		adViewPager.setOnPageChangeListener(new AdPageChangeListener());
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (isContinue) {
						viewHandler.sendEmptyMessage(atomicInteger.get());
						atomicOption();

					}
				}
			}
		}).start();

	}

	protected void atomicOption() {
		atomicInteger.incrementAndGet();
		if (atomicInteger.get() > imageViews.length - 1) {
			atomicInteger.getAndAdd(-5);
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

		}
	}

	/**
	 * ViewPager ҳ��ı������
	 */
	private final class AdPageChangeListener implements OnPageChangeListener {

		/**
		 * ҳ�����״̬�����ı��ʱ�򴥷�
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		/**
		 * ҳ�������ʱ�򴥷�
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		/**
		 * ҳ��ѡ�е�ʱ�򴥷�
		 */
		@Override
		public void onPageSelected(int arg0) {
			// ��ȡ��ǰ��ʾ��ҳ�����ĸ�ҳ��
			atomicInteger.getAndSet(arg0);
			// ��������ԭ�㲼�ּ���
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.point_focused);
				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.point_unfocused);
				}
			}
		}
	}

	private void initCirclePoint(View view) {
		ViewGroup group = (ViewGroup) view.findViewById(R.id.viewGroup);
		imageViews = new ImageView[pageViews.size()];
		for (int i = 0; i < pageViews.size(); i++) {
			// ����һ��ImageView, �����ÿ��. ���ö�����뵽������
			imageView = new ImageView(getActivity());
			imageView.setLayoutParams(new LayoutParams(10, 10));
			imageViews[i] = imageView;

			// ��ʼֵ, Ĭ�ϵ�0��ѡ��
			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.point_focused);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.point_unfocused);
			}
			// ��СԲ����뵽������
			group.addView(imageViews[i]);
		}

	}

	private void initPageAdapter() {
		// TODO Auto-generated method stub
		pageViews = new ArrayList<View>();
		img = new ImageView[3];
		img[0] = new ImageView(getActivity());
		img[0].setImageResource(R.drawable.ad1);
		img[1] = new ImageView(getActivity());
		img[1].setImageResource(R.drawable.ad2);
		img[2] = new ImageView(getActivity());
		img[2].setImageResource(R.drawable.ad2);
		pageViews.add(img[0]);
		img[0].setScaleType(ScaleType.FIT_XY);
		img[1].setScaleType(ScaleType.FIT_XY);
		img[2].setScaleType(ScaleType.FIT_XY);
		pageViews.add(img[1]);
		pageViews.add(img[2]);
		adapter = new AdPageAdapter(pageViews);

	}

	private final class AdPageAdapter extends PagerAdapter {
		private List<View> views = null;

		/**
		 * ��ʼ������Դ, ��View����
		 */
		public AdPageAdapter(List<View> views) {
			this.views = views;
		}

		/**
		 * ��ViewPager��ɾ�������ж�Ӧ������View����
		 */
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}

		/**
		 * ��ȡViewPager�ĸ���
		 */
		@Override
		public int getCount() {
			return views.size();
		}

		/**
		 * ��View�����л�ȡ��Ӧ������Ԫ��, ����ӵ�ViewPager��
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(views.get(position), 0);
			return views.get(position);
		}

		/**
		 * �Ƿ���ʾ��ViewPagerҳ����instantiateItem���صĶ�����й��� ��������Ǳ���ʵ�ֵ�
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}
}
