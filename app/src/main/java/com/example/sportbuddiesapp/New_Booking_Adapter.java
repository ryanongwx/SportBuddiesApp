package com.example.sportbuddiesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class New_Booking_Adapter extends RecyclerView.Adapter<New_Booking_Adapter.BookingViewHolder>{

    private Context c;
    private List<Booking> bookingList;
    private OnNoteListener mOnNoteListener;

    public New_Booking_Adapter(Context c, List<Booking> bookingList, OnNoteListener onNoteListener) {
        this.c = c;
        this.bookingList = bookingList;
        this.mOnNoteListener = onNoteListener;
    }


    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        View view = layoutInflater.inflate(R.layout.bookings_row_layout, parent, false);
        return new BookingViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {

        Booking booking = bookingList.get(position);
        holder.MyBookingStatus.setText(booking.getBookingstatus());
        holder.MyCurrentPlayerNumber.setText(booking.getPlayernumber().toString());
        holder.MyDate.setText(booking.getDate());
        holder.MySportLocation.setText(booking.getLocation());
        holder.MyTime.setText(booking.getTimeslot());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView MySportLocation, MyTime, MyDate, MyCurrentPlayerNumber, MyBookingStatus;
        OnNoteListener onNoteListener;

        public BookingViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            MySportLocation = itemView.findViewById(R.id.bookingSportLocation);
            MyTime = itemView.findViewById(R.id.bookingTime);
            MyDate = itemView.findViewById(R.id.bookingDate);
            MyCurrentPlayerNumber = itemView.findViewById(R.id.bookingPlayers);
            MyBookingStatus = itemView.findViewById(R.id.bookingStatus);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

}
