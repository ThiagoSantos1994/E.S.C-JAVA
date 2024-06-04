UPDATE
   tbd_ObservacaoDetalheDespesaMensal
SET
   ds_AnotacaoDespesa = :detalhe.dsObservacoes,
   ds_DataHoraAlteracao = GETDATE()
WHERE
   id_Despesa = :detalhe.idDespesa
   AND id_DetalheDespesa = :detalhe.idDetalheDespesa
   AND id_Ordem = :detalhe.idOrdem
   AND id_Funcionario = :detalhe.idFuncionario
