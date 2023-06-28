package swen221.chessview.moves;

import swen221.chessview.*;
import swen221.chessview.pieces.*;

public class Castling implements MultiPieceMove {
	private boolean isWhite;
	private boolean kingSide;

	public Castling(boolean isWhite, boolean kingSide) {
		this.isWhite = isWhite;
		this.kingSide = kingSide;
	}

	@Override
	public boolean isWhite() {
		return isWhite;
	}

	@Override
	public void apply(Board board) {
		int row = isWhite ? 1 : 8;
		int rcol = kingSide ? 8 : 1;
		int rncol = kingSide ? 6 : 4;
		int kncol = kingSide ? 7 : 3;

		Position kpos = new Position(row,5);
		Position knpos = new Position(row,kncol);
		Position rpos = new Position(row,rcol);
		Position rnpos = new Position(row,rncol);

		board.move(kpos, knpos);
		board.move(rpos, rnpos);
	}

	@Override
	public boolean isValid(Board board) {
		int row = isWhite ? 1 : 8;
		int col = kingSide ? 8 : 1;
		Position kpos = new Position(row,5);
		Position rpos = new Position(row,col);
		Piece king = board.pieceAt(kpos);
		Piece rook = board.pieceAt(rpos);

		return king != null && rook != null 
				&& king.equals(new King(isWhite))
				&& rook.equals(new Rook(isWhite))
				&& board.clearRowExcept(kpos, rpos, king, rook)
				&& !board.kingMoved(isWhite)
				&& !board.rookMoved(isWhite,kingSide);
	}

	@Override
	public String toString() {
		if(kingSide) {
			return "O-O";
		} else {
			return "O-O-O";
		}
	}
}
