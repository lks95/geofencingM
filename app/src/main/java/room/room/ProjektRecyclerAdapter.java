package room.room;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adesso.lklein.geofencing.R;

import java.util.List;

import room.room.model.Projekt;

public class ProjektRecyclerAdapter extends RecyclerView.Adapter<ProjektRecyclerAdapter.ViewHolder>{

    interface ActionCallback {
        void onLongClickListener(Projekt projekt);
    }

    private Context context;
    private List<Projekt> projektList;
    private int[] colors;
    private ActionCallback mActionCallback;

    ProjektRecyclerAdapter(Context context, List<Projekt> projektList, int[] colors){

        this.context = context;
        this.projektList = projektList;
        this.colors = colors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.room_item_recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.bindData(position);
    }

    @Override
    public int getItemCount(){
        return projektList.size();
    }

    void updateData(List<Projekt> projekte){
        this.projektList = projekte;
        notifyDataSetChanged();
    }


    //ViewHolder class

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        private TextView mNameTextView;
        private TextView mInitialsTextView;
        private GradientDrawable mInitialsBackground;

        ViewHolder(View itemView){
            super(itemView);
            itemView.setOnLongClickListener(this);

            mInitialsTextView = itemView.findViewById(R.id.initialsTextView);
            mNameTextView = itemView.findViewById(R.id.nameTextView);
            mInitialsBackground = (GradientDrawable) mInitialsTextView.getBackground();
        }

        void bindData(int position){
            Projekt projekt = projektList.get(position);

            String fullName = projekt.getProjektname() + " " + projekt.getName();
            mNameTextView.setText(fullName);

            String initial = projekt.getProjektname().toUpperCase().substring(0, 1);
            mInitialsTextView.setText(initial);


            mInitialsBackground.setColor(colors[position % colors.length]);
        }




        @Override
        public boolean onLongClick(View v) {
            if(mActionCallback != null){
                mActionCallback.onLongClickListener(projektList.get(getAdapterPosition()));
            }
            return true;
        }
    }

    void addActionCallback(ActionCallback actionCallbacks) {
        mActionCallback = actionCallbacks;
    }
}
