DELETE FROM
    tbd_DetalheDespesasMensais
WHERE
   id_Parcela = :idParcela
   AND id_DespesaParcelada = :idDespesaParcelada
   AND id_Funcionario = :idFuncionario