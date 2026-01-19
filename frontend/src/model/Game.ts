import type { Player } from "./Player"

export type Status = "NOT_STARTED" | "STARTED" | "FINISHED"

export type Game = {
  id: number,
  gameStatus: Status,
  currentPlayer: Player,
  pins: number,
  frame: number,
  players: Player[]
}
