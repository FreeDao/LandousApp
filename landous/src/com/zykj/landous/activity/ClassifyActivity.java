package com.zykj.landous.activity;

import com.zykj.landous.R;
import com.zykj.landous.classify.AnimationSildingLayout;
import com.zykj.landous.classify.LeftViewAdapter;
import com.zykj.landous.classify.RightListAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ClassifyActivity extends BaseActivity {

	public ListView rightList;

	ListView leftList;
	RightListAdapter bb;
	LeftViewAdapter aa;
	public int foodpoition;
	public String leixingName;

	private int last_item = -1;
	private TextView oldView;
	String cities[][] = new String[][] {
			new String[] { "ȫ����ʳ", "���ｭ���", "����", "����", "���", "������", "̨���",
					"�½�/����", "�ز�", "���", "������", "С�Կ��", "�ձ�", "��������", "�����ǲ�",
					"����", "������", "����" },
			new String[] { "ȫ����������", "������", "�ư�", "���", "KTV", "��ӰԺ", "��������",
					"��԰", "����/����", "ϴԡ", "��ԡ��Ħ", "�Ļ�����", "DIY�ֹ���", "�����",
					"������Ϸ", "������������" },
			new String[] { "ȫ������", "�ۺ��̳�", "����Ь��", "�˶�����", "�鱦��Ʒ", "��ױƷ",
					"����ҵ�", "���ӹ���", "�Ҿӽ���", "���", "���", "�۾���", "��ɫ����",
					"���๺�ﳡ��", "ʳƷ���", "����/������", "ҩ��" },
			new String[] { "ȫ����������", "������", "�ư�", "���", "KTV", "��ӰԺ", "��������",
					"��԰", "����/����", "ϴԡ", "��ԡ��Ħ", "�Ļ�����", "DIY�ֹ���", "�����",
					"������Ϸ", "������������" },
			new String[] { "ȫ", "������", "�ư�", "���", "KTV", "��������", "��԰",
					"����/����", "ϴԡ", "��ԡ��Ħ", "�Ļ�����", "DIY�ֹ���", "�����", "������Ϸ",
					"������������" },
			new String[] { "ȫ��", "������", "�ư�", "���", "��ӰԺ", "��������", "��԰",
					"����/����", "ϴԡ", "��ԡ��Ħ", "�Ļ�����", "DIY�ֹ���", "�����", "������Ϸ",
					"������������" },
			new String[] { "ȫ����", "������", "�ư�", "���", "KTV", "��ӰԺ", "��������",
					"��԰", "����/����", "ϴԡ", "��ԡ��Ħ", "�Ļ�����", "DIY�ֹ���", "�����",
					"������Ϸ", "������������" },
			new String[] { "ȫ������", "������", "�ư�", "���", "KTV", "��ӰԺ", "��������",
					"��԰", "����/����", "ϴԡ", "��ԡ��Ħ", "�Ļ�����", "DIY�ֹ���", "�����",
					"������Ϸ", "������������" },
			new String[] { "ȫ��������", "������", "�ư�", "���", "KTV", "��ӰԺ", "��������",
					"��԰", "����/����", "ϴԡ", "��ԡ��Ħ", "�Ļ�����", "DIY�ֹ���", "�����",
					"������Ϸ" },
			new String[] { "ȫ����������", "������", "�ư�", "���", "KTV", "��ӰԺ", "��������",
					"��԰", "����/����", "ϴԡ", "��ԡ��Ħ", "�Ļ�����", "DIY�ֹ���", "�����",
					"������Ϸ", "������������" },
			new String[] { "ȫ������aaa", "������", "�ư�", "���", "KTV", "��ӰԺ", "��������",
					"��԰", "����/����", "ϴԡ", "��ԡ��Ħ", "�Ļ�����", "DIY�ֹ���", "�����",
					"������Ϸ" }, };
	String food[] = new String[] { "ȫ��Ƶ��", "��ʳ", "��������", "����", "�Ƶ�", "����",
			"�˶�����", "���", "����", "����", "�������", "��ʳ", "��������", "����", "�Ƶ�", "����",
			"�˶�����", "���", "����", "����", "�������" };

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.food);

		leftList = (ListView) findViewById(R.id.list);
		rightList = (ListView) findViewById(R.id.list1);
		final AnimationSildingLayout layout = (AnimationSildingLayout) findViewById(R.id.main_slayout);
		layout.initLayout(leftList, rightList);
		layout.setOnSildingFinishListener(new AnimationSildingLayout.OnSildingFinishListener() {
			@Override
			public void onSildingFinish() {
				// todo ����rightview �Ƴ�������߼�
			}
		});

		aa = new LeftViewAdapter(ClassifyActivity.this, food, null, last_item);

		leftList.setAdapter(aa);
		leftList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				foodpoition = position;
				aa.setSelectedPosition(position);
				aa.notifyDataSetInvalidated();
				bb = new RightListAdapter(ClassifyActivity.this, cities,
						foodpoition);

				rightList.setDivider(null);
				rightList.setAdapter(bb);

				layout.startSildingInAnimation(position);
			}
		});
		rightList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(), cities[foodpoition%10][position], Toast.LENGTH_LONG).show();
				
			}
		});
		// update();
	}

}
