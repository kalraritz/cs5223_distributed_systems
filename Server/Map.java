package Server;

import java.util.Random;

/**
 * The whole map of the game
 * @param grid Grid[][] provide the information of positions of user and treasure
 * @author Oyang
 *
 */
public class Map {
	public Grid[][] grids;
	
	public Map(int N_Grid, int M_Treasure){
		
		this.grids = new Grid[N_Grid][N_Grid];
		for(int i = 0; i < N_Grid; i ++)
			for(int j = 0; j < N_Grid; j ++){
				grids[i][j] = new Grid();
			}
		
		//fill the map with random treasures
		int treasureAllocated = 0;
		int currTreasureGenerated = 0;
		int posXGenerated = 0;
		int posYGenerated = 0;
		Random randomGeneratorPosX = new Random();
		Random randomGeneratorPosY = new Random();
		Random randomGeneratorTreasureVal = new Random();
		
		while(treasureAllocated < M_Treasure){
			if(M_Treasure - treasureAllocated < 5){
				currTreasureGenerated = randomGeneratorTreasureVal.nextInt(M_Treasure - treasureAllocated + 1);
			} else{
				currTreasureGenerated = randomGeneratorTreasureVal.nextInt(5);
			}
			if(currTreasureGenerated > 0){
				treasureAllocated += currTreasureGenerated;
				posXGenerated = randomGeneratorPosX.nextInt(N_Grid);
				posYGenerated = randomGeneratorPosY.nextInt(N_Grid);
				grids[posXGenerated][posYGenerated].treasure += currTreasureGenerated;
			}
		}

		
	}
	// return the whole map situation as Grid
	public Grid[][] getGrids(){
		return this.grids;
	}
	
	protected void setTreasures(int pi, int pj){
		this.grids[pi][pj].treasure ++;
	}
	
	protected void clearTreasure(int pi, int pj){
		this.grids[pi][pj].treasure = 0;
	}
		
	protected synchronized void moveUser(Integer userID, int before_pi, int before_pj, int after_pi, int after_pj){
		this.grids[before_pi][before_pj].user = userID;
		this.grids[after_pi][after_pj].user = userID;
	}
	
	protected void removeUser(int pi, int pj){
		this.grids[pi][pj].user = 0;
	}
	
	
}

