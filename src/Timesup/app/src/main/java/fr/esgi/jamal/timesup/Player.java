package fr.esgi.jamal.timesup;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jamal on 20/05/2016.
 */
public class Player implements Parcelable
{
    private static int  sm_id = 0;
    private int         m_id;
    private String      m_name;
    private int         m_lastScore;

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public Player(final String name)
    {
        this.m_name =       name;
        this.m_lastScore =  0;
        this.m_id = this.sm_id;
        this.sm_id++;
    }

    public Player(final Parcel data)
    {
        this.m_id = data.readInt();
        m_name = data.readString();
        m_lastScore = data.readInt();
    }

    public Player(final Player player)
    {
        this.m_id = this.sm_id;
        this.sm_id++;
        m_name = player.m_name;
        m_lastScore = player.m_lastScore;
    }

    public Player(final SharedPreferences prefs)
    {
        this.m_id = this.sm_id;
        this.sm_id++;
        load(prefs);
    }

    public final String getName() { return m_name; }
    public void setName(final String name) { m_name = name; }

    public final int getLastScore() { return m_lastScore; }
    public void setLastScore(int lastScore) { m_lastScore = lastScore; }

    public final int getId() { return m_id; }

    public Player load(final SharedPreferences prefs)
    {
        this.m_name = prefs.getString("playerName" + m_id, "UNDEFINED");
        this.m_lastScore = prefs.getInt("playerLastScore" + m_id, 0);
        return this;
    }

    public final Player store(final SharedPreferences prefs)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("playerName" + m_id, m_name);
        editor.putInt("playerLastScore" + m_id, m_lastScore);
        int sharedPrefsLoadCount = prefs.getInt("sharedPrefsLoadCount", 0);
        editor.putInt("sharedPrefsLoadCount", ++sharedPrefsLoadCount);
        editor.commit();
        return this;
    }

    public final String toString()
    {
        return m_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(m_id);
        out.writeString(m_name);
        out.writeInt(m_lastScore);
    }
}
