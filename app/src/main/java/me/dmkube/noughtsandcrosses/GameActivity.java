package me.dmkube.noughtsandcrosses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private List<TextView> cells = new ArrayList<>();
    private static final String CELL_NOUGHT = "O";
    private static final String CELL_CROSS = "X";
    private static final String CELL_EMPTY = "";
    private boolean crossesGo = true;

    // 1  2   4
    // 8  16  32
    // 64 128 256
    private static final Integer[] winValues = {7, 56, 448, 73, 146, 292, 273, 84};
    private static final List<Integer> WIN_VALUES = Arrays.asList(winValues);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        cells.add((TextView) findViewById(R.id.row1col1));
        cells.add((TextView) findViewById(R.id.row1col2));
        cells.add((TextView) findViewById(R.id.row1col3));
        cells.add((TextView) findViewById(R.id.row2col1));
        cells.add((TextView) findViewById(R.id.row2col2));
        cells.add((TextView) findViewById(R.id.row2col3));
        cells.add((TextView) findViewById(R.id.row3col1));
        cells.add((TextView) findViewById(R.id.row3col2));
        cells.add((TextView) findViewById(R.id.row3col3));

        for(TextView cell: cells) {
            cell.setOnClickListener(this);
        }

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        reset();
    }

    private void reset() {
        for(TextView cell: cells) {
            cell.setText(CELL_EMPTY);
        }
        crossesGo = true;
    }

    @Override
    public void onClick(View view) {
        TextView textView = (TextView) view;
        Log.d("app", String.format("%s pressed", textView.getId()));

        if (!textView.getText().equals(CELL_EMPTY)) {
            // cell around used
            return;
        }

        if (crossesGo) {
            textView.setText(CELL_CROSS);
        } else {
            textView.setText(CELL_NOUGHT);
        }
        checkForWin();

        // swap player
        crossesGo = !crossesGo;
    }

    private void checkForWin() {
        int crossScore = 0;
        int noughtScore = 0;

        for(TextView cell: cells) {
            int binaryValue = Integer.parseInt((String) cell.getTag());
            if (cell.getText().equals(CELL_CROSS)) {
                crossScore += binaryValue;
            } else if (cell.getText().equals(CELL_NOUGHT)) {
                noughtScore += binaryValue;
            }
        }

        boolean crossWins = false;
        boolean noughtWins = false;
        if (WIN_VALUES.indexOf(crossScore) > -1) {
            Log.d("app", String.format("Cross WINS! total binary score = %s", crossScore));
            crossWins = true;
        } else if (WIN_VALUES.indexOf(noughtScore) > -1) {
            Log.d("app", String.format("Nought WINS! total binary score = %s", crossScore));
            noughtWins = true;
        }

        if (crossWins || noughtWins) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(String.format("%s WINS!", crossWins ? "Cross" : "Nought"))
                   .setTitle("WINNER")
                    .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Ignore
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
