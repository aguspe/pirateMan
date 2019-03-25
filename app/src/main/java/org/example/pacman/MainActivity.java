package org.example.pacman;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //reference to the main view
    GameView gameView;
    TextView textView;
    TextView reminding;
    private int counter = 0;
    private int ghostCounter = 0;
    public int countDown=60;
    public int finalCount = 1;
    //reference to the game class.
    Game game;
    private Timer myTimer;
    private Timer countTime;
    private Timer ghostTimer;
    private Timer Handler;

    private SoundPlayer soundPlayer;

    public int lastChangedDirectionTime = 0;
    public final int periodToChangeDirectionTime = 200;
    private boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        soundPlayer = new SoundPlayer(this);

        textView = findViewById(R.id.textView);
        reminding = findViewById(R.id.Count);

        gameView =  findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);

        game = new Game(this,textView, soundPlayer);
        game.setGameView(gameView);
        gameView.setGame(game);

        game.newGame();

        myTimer = new Timer();
        countTime = new Timer();
        ghostTimer = new Timer();
        Handler = new Timer();

        running = true; //should the game be running?
        //We will call the timer 5 times each second

        ghostTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                GhostTimerMethod(

                );
            }
        }, 0, 200);

        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod(

                );
            }
        }, 0, 200);

        countTime.schedule(new TimerTask() {
            @Override
            public void run() {
                CountMethod(

                );
            }
        },0, 1000);


        Button buttonRight = findViewById(R.id.moveRight);
        //listener of our pacman, when somebody clicks it
        buttonRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                game.direction = game.RIGHT;
                game.movePacmanRight(10);
                soundPlayer.playPacMoveSound();
            }
        });

        Button buttonLeft = findViewById(R.id.moveLeft);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                game.direction = game.LEFT;
                game.movePacmanLeft(10);
                soundPlayer.playPacMoveSound();
            }
        });

        Button buttonUp = findViewById(R.id.moveUp);
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.direction = game.UP;
                game.movePacmanUp(10);
                soundPlayer.playPacMoveSound();
            }
        });

        Button buttonDown = findViewById(R.id.moveDown);
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.direction = game.DOWN;
                game.movePacmanDown(10);
                soundPlayer.playPacMoveSound();
            }
        });

        findViewById(R.id.startButton).setOnClickListener(this);
        findViewById(R.id.stopButton).setOnClickListener(this);
        findViewById(R.id.resetButton).setOnClickListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        myTimer.cancel();
        countTime.cancel();
        ghostTimer.cancel();
    }

    private void TimerMethod()
    {
        this.runOnUiThread(Timer_Tick);
    }

    private void CountMethod() {
        this.runOnUiThread(Timer_Count);
    }

    private void GhostTimerMethod(){
        this.runOnUiThread(Ghost_Tick);
    }

    private Runnable Ghost_Tick = new Runnable() {
        @Override
        public void run() {
            if (running){

                for(int i =0; i<5; i++){
                    int test=new Random().nextInt(5);
                    lastChangedDirectionTime+= ghostCounter++;
                    if (lastChangedDirectionTime>= periodToChangeDirectionTime){
                        switch(test) {
                            case 1:
                                game.ghostDirection=game.GUP;
                                game.moveGhostUp(5);
                                Log.d("its running", "run: im running");
                                break;
                            case 2:
                                game.ghostDirection=game.GLEFT;
                                game.moveGhostLeft(5);
                                Log.d("its running left", "run: im running left");
                                break;
                            case 3:
                                game.ghostDirection=game.GDOWN;
                                game.moveGhostDown(5);
                                Log.d("its running down", "run: im running down");
                                break;
                            case 4:
                                game.ghostDirection=game.GRIGHT;
                                game.moveGhostRight(5);
                                Log.d("its running right", "run: im running rigth");
                                break;
                        }

                        lastChangedDirectionTime=0;
                    }
                }
            }
        }
    };

    public Runnable Timer_Count = new Runnable() {
        @Override
        public void run() {
            if (running){
                countDown --;
                reminding.setText("Argg check your time pirate: "+countDown);
                if(countDown == 5){
                    reminding.setText("Argg your time is almost up pirate: " +countDown);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            reminding.setText("Time is up pirate!");
                            game.newGame();
                            countDown = 60;
                            counter = 0;
                        }
                    }, 5000);   //5 seconds
                }
            }
        }
    };

    public Runnable Timer_Tick = new Runnable() {
        public void run() {
            if (running)
            {
                counter++;
                textView.setText("Time on the sea: "+counter);

                if (game.direction==game.UP)
                    game.movePacmanUp(10);

                if (game.direction==game.DOWN)
                    game.movePacmanDown(10);

                if (game.direction==game.RIGHT)
                    game.movePacmanRight(10);

                if (game.direction==game.LEFT)
                    game.movePacmanLeft(10);

//                    still within our boundaries?
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"settings clicked",Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_newGame) {
            TextView textView = findViewById(R.id.points);//we initialize new texview and connect to the  new points text view
            Toast.makeText(this,"New Game started",Toast.LENGTH_LONG).show();//when we click on new game we make toast
            game = new Game(this, textView,soundPlayer);// we give a new value to a new game
            game.setGameView(gameView);
            gameView.setGame(game);

            countDown = 60;
            ghostCounter = 0;
            counter = 0;
            gameView.reset();
            running = true;
            textView.setText("Timer value: "+counter);
            reminding.setText("Time remaining: "+countDown);

            game.newGame();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.startButton)
        {
            running = true;
        }
        else if (v.getId()==R.id.stopButton)
        {
            running = false;
        }
        else if (v.getId()==R.id.resetButton)
        {
            countDown = 60;
            ghostCounter = 0;
            counter = 0;
            gameView.reset();
            running = false;
            textView.setText("Timer value: "+counter);
            reminding.setText("Time remaining: "+countDown);
        }

    }
}


