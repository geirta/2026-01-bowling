import { useEffect, useState } from "react";
import type { Game } from "../model/Game";
import type { Player } from "../model/Player";
import 'bootstrap/dist/css/bootstrap.min.css';

const backendUrl = import.meta.env.VITE_API_HOST;

function Home() {

  const [playerName, setPlayerName] = useState("");
  const [game, setGame] = useState<Game | null>(null);
  const [player, setPlayer] = useState<Player | null>(null);

  const addPlayer = () => {
    fetch(`${backendUrl}/players`, {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify({name: playerName})
    })
    .then(() => {
      setPlayerName("");
    })
  }

  const resetGame = () => {
    fetch(`${backendUrl}/games`, {
      method: "DELETE"
    })
  }

  useEffect(() => {
    fetch(`${backendUrl}/games`)
      .then(res => res.json())
      .then(json => setGame(json))
  }, [game]);

  const startGame = async () => {
    const res = await fetch(`${backendUrl}/games/new`);
    const data: Player = await res.json();
    setPlayer(data);
  }

  const throwBall = async () => {
    const res = await fetch(`${backendUrl}/games/throw`);
    const data = await res.json();
    setGame(data);
  }



  return (
    <div className='container'>
      <div className="scoreBoard">
        {/* <h1 className='text-center'>Scoreboard</h1> */}
        <table className='table table-bordered'>
          <thead className='tableHead'>
            <tr>
              <th>Name</th>
              <th>F1</th>
              <th>F2</th>
              <th>F3</th>
              <th>F4</th>
              <th>F5</th>
              <th>F6</th>
              <th>F7</th>
              <th>F8</th>
              <th>F9</th>
              <th>F10</th>
            </tr>
          </thead>
          <tbody>
            {game?.players && game.players.length > 0 
              ? (game.players.map(player => (

                  <tr key={player.name}>
                    <td className="playerName">{player.name}</td>

                    
                    {Array.from({ length: 10 }).map((_, i) => (
                      <td key={i}>
                        const frameScore = player.frameScores?.[i];
                        <div className="frame-throws">
                          {player.frames[i]?.[0] === 10 
                            ? (<span className="throw">X</span>) 
                            : (<span className="throw">{player.frames[i]?.[0] ?? ""}</span>)
                          }
                          {/* <span className="throw">{player.frames[i]?.[0] ?? ""}</span> */}
                          {player.frames[i]?.[0] !== undefined && player.frames[i]?.[1] !== undefined && player.frames[i][0] + player.frames[i][1] === 10 
                            ?  (<span className="throw">/</span>) 
                            : (<span className="throw">{player.frames[i]?.[1] ?? ""}</span>)
                          }
                          {/* <span className="throw">{player.frames[i]?.[1] ?? ""}</span> */}
                          {player.frames[i]?.[2] !== undefined && (
                            <span>{player.frames[i][2]}</span>
                          )}
                        </div>
                        <div className="frame-score">
                          {player.frameScores?.[i] ?? ""}
                        </div>
                      </td>
                    ))}
                  </tr>
                )))
              :
                (
                  <tr>
                    <td colSpan={11} className="text-center">
                      No players yet
                    </td>
                  </tr>
                )
            }
          </tbody>
        </table>
      </div>
      
      {(game?.gameStatus === "NOT_STARTED" || game?.gameStatus === null) && (
        <div className="setup">
          <label>Player name: </label>
          <input
            value={playerName} 
            onChange={e => setPlayerName(e.target.value)} 
            placeholder="Enter name here">
          </input>
          <button className="btnStyle" onClick={addPlayer} disabled={!playerName.trim()}>Add player</button>
          <div>
          <button onClick={startGame} disabled={!game?.players || game.players.length === 0}>Start game</button>
        </div>
        </div>
      )}

      {game?.gameStatus === "STARTED" && (
      <div className="turns">
        <h1>Baller:</h1>
        <h2>{game.currentPlayer.name}</h2>
        <div>
          <button className="btnStyle" onClick={throwBall}>Throw ball</button>
        </div>
        <div>
          <div>Pins up: {game.pins}</div>
        </div>
      </div>
      )}
      
      
      {(game?.players?.length ?? 0) > 0 && (
        <div className="ftr">
          <button className="btnStyle" onClick={resetGame}>New game</button>
        </div>
      )}

    </div>
  )
}

export default Home
