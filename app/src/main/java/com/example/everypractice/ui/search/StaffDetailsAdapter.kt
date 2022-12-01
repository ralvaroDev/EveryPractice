package com.example.everypractice.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.everypractice.data.models.TemporaryStaffMovie
import com.example.everypractice.databinding.ItemStaffMovieBinding

class StaffDetailsAdapter(
    private val listStaff: List<TemporaryStaffMovie>,
    private val onItemStaffSelected: (id:Int) -> Unit
) : RecyclerView.Adapter<StaffDetailsAdapter.StaffDetailsViewHolder>() {

    class StaffDetailsViewHolder(private val binding: ItemStaffMovieBinding) :
        RecyclerView.ViewHolder(binding.root){

            fun bind(
                staff: TemporaryStaffMovie,
                onItemStaffSelected: (id:Int) -> Unit
            ){
                binding.ivImageActor.load(staff.profilePath)
                binding.tvRoleInMovie.text = staff.character
                binding.tvRealName.text = staff.originalName
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffDetailsViewHolder {
        return StaffDetailsViewHolder(
            ItemStaffMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: StaffDetailsViewHolder, position: Int) {
        holder.bind(listStaff[position],onItemStaffSelected)
    }

    override fun getItemCount(): Int {
        return listStaff.size
    }
}