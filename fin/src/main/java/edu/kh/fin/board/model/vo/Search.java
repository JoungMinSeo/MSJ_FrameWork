package edu.kh.fin.board.model.vo;

import java.util.Arrays;

public class Search {
	private String sk;
	private String sv;
	private int boardType;
	private int[] ct;
	
	public Search() {
		// TODO Auto-generated constructor stub
	}

	public String getSk() {
		return sk;
	}

	public void setSk(String sk) {
		this.sk = sk;
	}

	public String getSv() {
		return sv;
	}

	public void setSv(String sv) {
		this.sv = sv;
	}

	public int getBoardType() {
		return boardType;
	}

	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}

	public int[] getCt() {
		return ct;
	}

	public void setCt(int[] ct) {
		this.ct = ct;
	}

	@Override
	public String toString() {
		return "Search [sk=" + sk + ", sv=" + sv + ", boardType=" + boardType + ", ct=" + Arrays.toString(ct) + "]";
	}
	
}
