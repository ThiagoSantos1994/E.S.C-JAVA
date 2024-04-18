UPDATE
   tbd_ConfiguracoesLancamentos
SET
   dt_ViradaMes = :parametro.dataViradaMes,
   tp_ViradaAutomatica = :parametro.viradaAutomatica
WHERE
   id_Funcionario = :parametro.idFuncionario
