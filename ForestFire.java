public class ForestFire extends CAtoolbox {

	

	public static void main(String[] args) {

		int dx = 50;
		int dy = 50;
		
		int[][] tableauCourant = new int[dx][dy];
		int[][] nouveauTableau = new int[dx][dy];
		int [][] altitude = new int [dx][dy];
		int [][] water = new int [dx][dy];
		
		int delai = 100;
		
		int nombreDePasMaximum = 10000;//1000;
		int it = 0;  /*Math.abs(alt[x][y] - alt[x][y+1]) <= 10;*/
		
		double densite = 0.40; //0.55; // seuil de percolation � 0.55

		
		// optionnel: initialise la visualisation dan && Math.abs(alt[x][y] - alt[x][y+1]) <= 1s une fenetre
		
		CAImageBuffer image = new CAImageBuffer(dx,dy);
	    ImageFrame imageFrame =	ImageFrame.makeFrame( "Forest fire", image, delai, 200, 200 );

		// initialisation (peuple la foret)
	    
	    for ( int x = 0 ; x != dx ; x++ )
	    	for ( int y = 0 ; y != dy ; y++ ) {
	    		if ( densite >= Math.random() )
	    			tableauCourant[(int)x][(int)y]=1; // tree
	    		altitude [x][y]= (int)(Math.random()*4);
	    		water [x][y]= (int)(Math.random()*4);
	    	}

	    tableauCourant[dx/2][dy/2] = 2; // burning tree
	    
		// on fait tourner l'automate
	    
	    double pf=0;
		
		while ( it != nombreDePasMaximum )
		{
			// 1a - affiche de l'�tat courant dans la fenetre graphique (toutes les cellules)
			image.updateForest(tableauCourant);
			
			// 1 - mise a jour de l'automate (dans le tableau en tampon)
			for ( int x = 0 ; x != tableauCourant.length ; x++ ){
				spreading ((int)((x+Math.random()*50)%water.length-1), (int)((Math.random()*50)%water[x].length-1), water, altitude);
				
				for ( int y = 0 ; y != tableauCourant[0].length ; y++ )
				{
					if (tableauCourant [x][y]==0){
						if ((float)Math.random()<=0.10)nouveauTableau[x][y]=1;
						else{ nouveauTableau[x][y]=0;}
					}
					if (tableauCourant [x][y]==1){
						if(surrounding(x, y, tableauCourant,altitude)!=0)
							nouveauTableau[x][y]=2;
						else{
						if ((float)Math.random()<=0.01)nouveauTableau[x][y]=2;
							else{ nouveauTableau[x][y]=1;}
							
						}
					}
					if (tableauCourant [x][y]==2)nouveauTableau[x][y]=3;
					if (tableauCourant [x][y]==3)nouveauTableau[x][y]=4;
					if (tableauCourant [x][y]==4)nouveauTableau[x][y]=5;
					if (tableauCourant [x][y]==5)nouveauTableau[x][y]=0;
					
					
					/* **** A COMPLETER **** */
					
				}
			}
			
			// 2 - met a jour le tableau affichable
			
			for ( int x = 0 ; x != tableauCourant.length ; x++ )
				for ( int y = 0 ; y != tableauCourant[0].length ; y++ )
					tableauCourant[x][y] = nouveauTableau[x][y];

			it++;
			
			// ne va pas trop vite...
			
			try {
				Thread.sleep(delai);
			} catch (InterruptedException e) 
			{
			}
		}
		
	}
	 
	public static int surrounding (int x, int y, int [][] tab, int[][] alt){
		if (x>0)
			if (tab[x-1][y]==2 && Math.abs(alt[x][y] - alt[x-1][y]) <= 1)return 1;
		if(x<tab.length-1)
			if (tab[x+1][y]==2 && Math.abs(alt[x][y] - alt[x+1][y]) <= 1)return 1;
		if (y>0)
			if (tab[x][y-1]==2 && Math.abs(alt[x][y] - alt[x][y-1]) <= 1)return 1;
		if(y<tab[x].length-1)
			if (tab[x][y+1]==2 && Math.abs(alt[x][y] - alt[x][y+1]) <= 1)return 1;
		return 0;
	}
	
	public static void spreading(int x, int y, int [][] wat, int [][] alt){
	int h=0;
	int[]t=new int [8];
	
	if (x>0){
		if ((wat [x][y]+alt[x][y]<wat [x-1][y]+alt[x-1][y])&&(alt[x-1][y]>0)){t[h]=0;h++;}
		if (y>0)
			if ((wat [x][y]+alt[x][y]<wat [x-1][y-1]+alt[x-1][y-1])&&(alt[x-1][y-1]>0)){t[h]=1;h++;}		
		if (y<wat[x].length-1)
			if ((wat [x][y]+alt[x][y]<wat [x-1][y+1]+alt[x-1][y+1])&&(alt[x-1][y+1]>0)){t[h]=2;h++;}
	}
		
	if(x<wat.length-1){
		if ((wat [x][y]+alt[x][y]<wat [x+1][y]+alt[x+1][y])&&(alt[x+1][y]>0)){t[h]=3;h++;}
		if ( y>0)
			if ((wat [x][y]+alt[x][y]<wat [x+1][y-1]+alt[x+1][y-1])&&(alt[x+1][y-1]>0)){t[h]=4;h++;}		
		if (y<wat[x].length-1)
			if ((wat [x][y]+alt[x][y]<wat [x+1][y+1]+alt[x+1][y+1])&&(alt[x+1][y+1]>0)){t[h]=5;h++;}
	}

	
	if (y>0)
		if ((wat [x][y]+alt[x][y]<wat [x][y-1]+alt[x][y-1])&&(alt[x][y-1]>0)){t[h]=6;h++;}
	if(y<wat[x].length-1)
		if ((wat [x][y]+alt[x][y]<wat [x][y+1]+alt[x][y+1])&&(alt[x][y+1]>0)){t[h]=7;h++;}
		
	int u= (int) (Math.random()*h);
	
	 if (t[u]==0){wat [x-1][y]+= 1; wat[x-1][y]-=1;}
	 if (t[u]==1){wat [x-1][y-1]+= 1; wat[x-1][y]-=1;}
	 if (t[u]==2){wat [x-1][y+1]+= 1; wat[x-1][y]-=1;}
	 if (t[u]==3){wat [x+1][y]+= 1; wat[x-1][y]-=1;}
	 if (t[u]==4){wat [x+1][y-1]+= 1; wat[x-1][y]-=1;}
	 if (t[u]==5){wat [x+1][y+1]+= 1; wat[x-1][y]-=1;}
	 if (t[u]==6){wat [x][y-1]+= 1; wat[x-1][y]-=1;}
	 if (t[u]==7){wat [x][y+1]+= 1; wat[x-1][y]-=1;}
	return;
	}
}
	


