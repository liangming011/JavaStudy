package 算法;

import java.util.Arrays;

public class Solution {

    public static boolean isValidSudoku(char[][] board) {
        //行
        for (int i = 0; i < 9; i++) {
            int sum1 = 0;
            for (int j = 0; j < 9; j++) {
                if ('.' == board[i][j]) {
                    board[i][j] = '0';
                }
                System.out.println(board[i][j]);
                int center = board[i][j] - '0';
                sum1 += center;
                if (sum1 > 45) {
                    return false;
                }
            }
            System.out.println();
        }

        //列
        for (int i = 0; i < 9; i++) {
            int sum2 = 0;
            for (int j = 0; j < 9; j++) {
                if ('.' == board[j][i]) {
                    board[j][i] = '0';
                }
                System.out.println(board[j][i]);
                int center = board[i][j] - '0';
                sum2 += center;
                if (sum2 > 45) {
                    return false;
                }
            }
            System.out.println();
        }
        System.out.println(Arrays.toString(board));
        //井
        for (int m = 0; m < 9; m = m + 3) {
            for (int n = 0; n < 9; n = n + 3) {
                for (int i = 0; m <= i && i <= m + 3; i++) {
                    int sum3 = 0;
                    for (int j = 0; n <= j && j <= n + 3; j++) {
                        if ('.' == board[j][i]) {
                            board[j][i] = '0';
                        }
                        System.out.println(board[j][i]);
                        int center = board[i][j] - '0';
                        sum3 += center;
                        if (sum3 > 45) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        char[][] board = {{'5', '3', '.', '.', '7', '.', '.', '.', '.'}, {'6', '.', '.', '1', '9', '5', '.', '.', '.'}, {'.', '9', '8', '.', '.', '.', '.', '6', '.'}, {'8', '.', '.', '.', '6', '.', '.', '.', '3'}, {'4', '.', '.', '8', '.', '3', '.', '.', '1'}, {'7', '.', '.', '.', '2', '.', '.', '.', '6'}, {'.', '6', '.', '.', '.', '.', '2', '8', '.'}, {'.', '.', '.', '4', '1', '9', '.', '.', '5'}, {'.', '.', '.', '.', '8', '.', '.', '7', '9'}};
        boolean test = isValidSudoku(board);
        System.out.println(test);
    }


}
