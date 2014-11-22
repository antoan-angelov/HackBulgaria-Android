package com.hackbulgaria.antoan.flappy;

/**
 * Created by Antoan on 12-Nov-14.
 */

import android.net.Uri;

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface FragmentInteractionListener {

    public final static int SCREEN_LOGIN = 1;
    public final static int SCREEN_GAME = 2;

    // TODO: Update argument type and name
    public void onScreenChange(int screen);
}
