DELETE FROM
    tbd_DetalheDespesasMensais
WHERE
   id_Despesa = :idDespesa
   AND tp_ParcelaAmortizada = 'S'
   AND id_DetalheDespesa = :idDetalheDespesa
   AND id_DespesaParcelada = :idDespesaParcelada
   AND id_Parcela = :idParcela
   AND id_Funcionario = :idFuncionario