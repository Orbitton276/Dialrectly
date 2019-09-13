package com.mta.sadna19.sadna.Adapter;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.mta.sadna19.sadna.MenuRegisters.DataOption;
import com.mta.sadna19.sadna.MenuRegisters.Option;
import com.mta.sadna19.sadna.R;

public class OptionAdapter extends BaseAdapter {
    Context context;
    Option m_Option;
    LayoutInflater inflter;
    onOptionSelectedListener m_optionSelected;
    OnOptionViewCreatedListener m_OptionViewCreated;

    public interface OptionViewHolder{
        public Option getOption();
        public View getView();
    }
    class TextOptionViewHolder implements OptionViewHolder{
        View m_View;
        Button m_name;
        Option m_Option;

        public Option getOption() {
            return m_Option;
        }
        public void setOption(Option m_Option) {
            this.m_Option = m_Option;
            updateView();
        }
        public View getView() {
            return m_View;
        }
        public TextOptionViewHolder(Option i_optionToSet){
            m_View = inflter.inflate(R.layout.option_list_item, null);
            m_name = m_View.findViewById(R.id.optionButton);
            m_Option = i_optionToSet;
            updateView();
        }
        private void updateView() {
            m_name.setText(m_Option.getName());
            m_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (m_optionSelected!=null){m_optionSelected.onItemSelected(m_Option);}
                }
            });
        }

        public OnOptionViewCreatedListener getM_OptionViewCreated() {
            return m_OptionViewCreated;
        }
    }
    public class DataOptionViewHolder implements OptionViewHolder{
        View m_View;
        Button m_Submit;
        EditText m_DataInput;
        DataOption m_Option;
        public Option getOption() {
            return m_Option;
        }
        public void SetDataText(String iStr){
            m_DataInput.setText(iStr);
        }
        public void SetData(DataOption m_Option) {
            this.m_Option = m_Option;
            updateView();
        }
        public View getView() {
            return m_View;
        }
        public DataOptionViewHolder(DataOption i_optionToSet){
            m_View = inflter.inflate(R.layout.dataoption_list_item, null);
            m_DataInput = m_View.findViewById(R.id.dataOptioneEditText);
            m_DataInput.setInputType(InputType.TYPE_CLASS_NUMBER);
            m_Submit = m_View.findViewById(R.id.dataOptionButton);

            SetData(i_optionToSet);
        }
        private void updateView() {
            m_DataInput.setHint(m_Option.getName());
            m_Submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_Option.addData(m_DataInput.getText().toString());
                    m_optionSelected.onItemSelected(m_Option);
                }
            });
        }
    }

    public OptionAdapter(Context applicationContext, Option i_Option) {
        this.context = context;
        this.m_Option = i_Option;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void updateAdapter(Option i_Option) {
        this.m_Option = i_Option;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return m_Option.getSubMenu().size();
    }

    @Override
    public Object getItem(int i) {
        return m_Option.getSubMenu().toArray()[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Option newOption = (Option) m_Option.getSubMenu().toArray()[i];
        OptionViewHolder viewHolder;
        switch (newOption.getType()){
            case "DataOption":
                viewHolder = new DataOptionViewHolder((DataOption) newOption);
                break;
            default:
                viewHolder = new TextOptionViewHolder(newOption);
                break;
        }
        view = viewHolder.getView();
        if (m_OptionViewCreated!=null){
            m_OptionViewCreated.OnOptionViewCreated(viewHolder);
        }
        return view;
    }

    public interface onOptionSelectedListener {
        void onItemSelected(Option i_op);
    }
    public interface OnOptionViewCreatedListener {
        void OnOptionViewCreated(OptionViewHolder iViewHolder);
    }

    public void setOnOptionSelected(onOptionSelectedListener i_onOptionSelectedListener){
        m_optionSelected  = i_onOptionSelectedListener;
    }
    public void setOnOptionViewCreatedListener(OnOptionViewCreatedListener i_OnOptionViewCreatedListener){
        m_OptionViewCreated  = i_OnOptionViewCreatedListener;
    }
}


